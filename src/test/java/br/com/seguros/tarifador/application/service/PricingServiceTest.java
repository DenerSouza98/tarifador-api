package br.com.seguros.tarifador.application.service;

import br.com.seguros.tarifador.application.ports.ProductRepository;
import br.com.seguros.tarifador.domain.model.Categoria;
import br.com.seguros.tarifador.domain.model.Product;
import br.com.seguros.tarifador.domain.pricing.TaxPolicyRegistry;
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

    @Test
    void deveCalcularPrecoTarifadoParaVida() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        Mockito.when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PricingService service = new PricingService(repo, new TaxPolicyRegistry());

        Product p = new Product(null, "Seguro Vida", Categoria.VIDA, new BigDecimal("100.00"), null);
        Product salvo = service.createProduct(p);

        assertEquals(new BigDecimal("103.20"), salvo.getPrecoTarifado());
    }

    @Test
    void deveIgnorarPrecoTarifadoDoRequest() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        Mockito.when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PricingService service = new PricingService(repo, new TaxPolicyRegistry());

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

        PricingService service = new PricingService(repo, new TaxPolicyRegistry());

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

        PricingService service = new PricingService(repo, new TaxPolicyRegistry());

        Product updates = new Product(null, "Qualquer", Categoria.VIDA, new BigDecimal("10.00"), null);

        assertThrows(EntityNotFoundException.class, () -> service.updateProduct(id, updates));
    }

    @Test
    void deveBuscarProdutoPorId() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        UUID id = UUID.randomUUID();
        Product p = new Product(id, "Residencial", Categoria.RESIDENCIAL, new BigDecimal("150.00"), new BigDecimal("160.50"));
        Mockito.when(repo.findById(id)).thenReturn(Optional.of(p));

        PricingService service = new PricingService(repo, new TaxPolicyRegistry());

        Product result = service.searchProduct(id);

        assertEquals(p, result);
    }

    @Test
    void deveLancarExcecaoAoBuscarProdutoInexistente() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        UUID id = UUID.randomUUID();
        Mockito.when(repo.findById(id)).thenReturn(Optional.empty());

        PricingService service = new PricingService(repo, new TaxPolicyRegistry());

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

        PricingService service = new PricingService(repo, new TaxPolicyRegistry());

        List<Product> result = service.listProduct();

        assertEquals(2, result.size());
        assertEquals("Vida", result.get(0).getNome());
    }

    @Test
    void deveDeletarProdutoPorId() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        UUID id = UUID.randomUUID();

        PricingService service = new PricingService(repo, new TaxPolicyRegistry());

        service.deleteProduct(id);

        Mockito.verify(repo).deleteById(id);
    }

}
