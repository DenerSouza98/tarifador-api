package br.com.seguros.tarifador.application.service;

import br.com.seguros.tarifador.application.ports.ProductRepository;
import br.com.seguros.tarifador.domain.model.Categoria;
import br.com.seguros.tarifador.domain.model.Product;
import br.com.seguros.tarifador.domain.pricing.TaxPolicyRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class PricingServiceTest {

    private PricingService newService(ProductRepository repo) {
        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        return new PricingService(repo, new TaxPolicyRegistry(), meterRegistry);
    }

    @Test
    void deveCalcularPrecoTarifadoParaVida() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        Mockito.when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PricingService service = newService(repo);

        Product p = new Product(null, "Seguro Vida", Categoria.VIDA, new BigDecimal("100.00"), null);
        Product salvo = service.createProduct(p);

        assertEquals(new BigDecimal("103.20"), salvo.getPrecoTarifado());
    }

    @Test
    void deveIgnorarPrecoTarifadoDoRequest() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        Mockito.when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PricingService service = newService(repo);

        Product p = new Product(null, "Auto", Categoria.AUTO, new BigDecimal("50.00"), new BigDecimal("999.99"));
        Product salvo = service.createProduct(p);

        assertEquals(new BigDecimal("55.25"), salvo.getPrecoTarifado());
    }

    @Test
    void deveAtualizarProdutoComTarifaCalculada() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        UUID id = UUID.randomUUID();
        Product atual = new Product(id, "Auto", Categoria.AUTO, new BigDecimal("50.00"), new BigDecimal("55.25"));
        Mockito.when(repo.findById(id)).thenReturn(Optional.of(atual));
        Mockito.when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PricingService service = newService(repo);

        Product updates = new Product(null, "Auto Atualizado", Categoria.VIAGEM, new BigDecimal("75.00"), null);
        Product salvo = service.updateProduct(id, updates);

        assertEquals("Auto Atualizado", salvo.getNome());
        assertEquals(Categoria.VIAGEM, salvo.getCategoria());
        assertEquals(new BigDecimal("80.25"), salvo.getPrecoTarifado());
    }

    @Test
    void deveLancarExcecaoAoAtualizarProdutoInexistente() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        UUID id = UUID.randomUUID();
        Mockito.when(repo.findById(id)).thenReturn(Optional.empty());

        PricingService service = newService(repo);

        Product updates = new Product(null, "Qualquer", Categoria.VIDA, new BigDecimal("10.00"), null);

        assertThrows(EntityNotFoundException.class, () -> service.updateProduct(id, updates));
    }

    @Test
    void deveBuscarProdutoPorId() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        UUID id = UUID.randomUUID();
        Product p = new Product(id, "Residencial", Categoria.RESIDENCIAL, new BigDecimal("150.00"), new BigDecimal("160.50"));
        Mockito.when(repo.findById(id)).thenReturn(Optional.of(p));

        PricingService service = newService(repo);

        Product result = service.searchProduct(id);

        assertEquals(p, result);
    }

    @Test
    void deveLancarExcecaoAoBuscarProdutoInexistente() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        UUID id = UUID.randomUUID();
        Mockito.when(repo.findById(id)).thenReturn(Optional.empty());

        PricingService service = newService(repo);

        assertThrows(EntityNotFoundException.class, () -> service.searchProduct(id));
    }

    @Test
    void deveListarProdutos() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        List<Product> lista = Arrays.asList(
                new Product(UUID.randomUUID(), "Vida", Categoria.VIDA, new BigDecimal("100.00"), new BigDecimal("103.20")),
                new Product(UUID.randomUUID(), "Auto", Categoria.AUTO, new BigDecimal("50.00"), new BigDecimal("55.25"))
        );
        Mockito.when(repo.findAll()).thenReturn(lista);

        PricingService service = newService(repo);

        List<Product> result = service.listProduct();

        assertEquals(2, result.size());
        assertEquals("Vida", result.get(0).getNome());
    }

    @Test
    void deveDeletarProdutoPorId() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        UUID id = UUID.randomUUID();

        PricingService service = newService(repo);

        service.deleteProduct(id);

        Mockito.verify(repo).deleteById(id);
    }

    @Test
    void deveIncrementarMetricaERepassarErroAoCriarProduto() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        PricingService service = new PricingService(repo, new TaxPolicyRegistry(), meterRegistry);

        Mockito.when(repo.save(any())).thenThrow(new RuntimeException("falha ao salvar"));

        Product p = new Product(null, "Seguro Vida", Categoria.VIDA, new BigDecimal("100.00"), null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createProduct(p));
        assertEquals("falha ao salvar", ex.getMessage());
        assertEquals(1.0, meterRegistry.get("pricing_errors_total").counter().count());
    }

    @Test
    void deveIncrementarMetricaERepassarErroAoDeletarProduto() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        PricingService service = new PricingService(repo, new TaxPolicyRegistry(), meterRegistry);

        UUID id = UUID.randomUUID();
        Mockito.doThrow(new RuntimeException("falha ao deletar")).when(repo).deleteById(id);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteProduct(id));
        assertEquals("falha ao deletar", ex.getMessage());
        assertEquals(1.0, meterRegistry.get("pricing_errors_total").counter().count());
    }

    @Test
    void deveIncrementarMetricaERepassarErroAoListarProdutos() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        PricingService service = new PricingService(repo, new TaxPolicyRegistry(), meterRegistry);

        Mockito.when(repo.findAll()).thenThrow(new RuntimeException("falha ao listar"));

        RuntimeException ex = assertThrows(RuntimeException.class, service::listProduct);
        assertEquals("falha ao listar", ex.getMessage());
        assertEquals(1.0, meterRegistry.get("pricing_errors_total").counter().count());
    }

}
