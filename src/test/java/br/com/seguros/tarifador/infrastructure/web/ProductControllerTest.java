package br.com.seguros.tarifador.infrastructure.web;

import br.com.seguros.tarifador.application.service.PricingService;
import br.com.seguros.tarifador.domain.model.Categoria;
import br.com.seguros.tarifador.domain.model.Product;
import br.com.seguros.tarifador.infrastructure.web.dto.ProductRequest;
import br.com.seguros.tarifador.infrastructure.web.dto.ProductResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class ProductControllerTest {

    @Mock
    private PricingService service;

    @Mock
    private MeterRegistry registry;

    @Mock
    private Counter counterCriado;

    @Mock
    private Counter counterAtualizado;

    private ProductController controller;

    private MockedStatic<Counter> counterStatic;
    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        Counter.Builder builderCriado = Mockito.mock(Counter.Builder.class);
        Counter.Builder builderAtualizado = Mockito.mock(Counter.Builder.class);

        counterStatic = Mockito.mockStatic(Counter.class);
        counterStatic.when(() -> Counter.builder("produtos_criados_total")).thenReturn(builderCriado);
        counterStatic.when(() -> Counter.builder("produtos_atualizados_total")).thenReturn(builderAtualizado);

        Mockito.when(builderCriado.description(Mockito.anyString())).thenReturn(builderCriado);
        Mockito.when(builderCriado.register(registry)).thenReturn(counterCriado);

        Mockito.when(builderAtualizado.description(Mockito.anyString())).thenReturn(builderAtualizado);
        Mockito.when(builderAtualizado.register(registry)).thenReturn(counterAtualizado);

        controller = new ProductController(service, registry);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (counterStatic != null) {
            counterStatic.close();
        }
        if (openMocks != null) {
            openMocks.close();
        }
    }

    @Test
    void deveCriarProduto() {
        ProductRequest req = new ProductRequest();
        req.setNome("Seguro Vida");
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(new BigDecimal("100.00"));

        Product produto = new Product(UUID.randomUUID(), req.getNome(), req.getCategoria(), req.getPrecoBase(), new BigDecimal("103.20"));
        Mockito.when(service.createProduct(any())).thenReturn(produto);

        ProductResponse resp = controller.createProduct(req);

        assertEquals("Seguro Vida", resp.nome());
        assertEquals(Categoria.VIDA, resp.categoria());
        Mockito.verify(service).createProduct(any());
        Mockito.verify(counterCriado).increment();
    }

    @Test
    void deveAtualizarProduto() {
        UUID id = UUID.randomUUID();
        ProductRequest req = new ProductRequest();
        req.setNome("Auto");
        req.setCategoria(Categoria.AUTO);
        req.setPrecoBase(new BigDecimal("50.00"));

        Product produto = new Product(id, req.getNome(), req.getCategoria(), req.getPrecoBase(), new BigDecimal("55.25"));
        Mockito.when(service.updateProduct(eq(id), any())).thenReturn(produto);

        ProductResponse resp = controller.updateProduct(id, req);

        assertEquals("Auto", resp.nome());
        assertEquals(Categoria.AUTO, resp.categoria());
        Mockito.verify(service).updateProduct(eq(id), any());
        Mockito.verify(counterAtualizado).increment();
    }

    @Test
    void deveBuscarProdutoPorId() {
        UUID id = UUID.randomUUID();
        Product produto = new Product(id, "Seguro Vida", Categoria.VIDA, new BigDecimal("100.00"), new BigDecimal("103.20"));
        Mockito.when(service.searchProduct(id)).thenReturn(produto);

        ProductResponse resp = controller.searchProduct(id);

        assertEquals("Seguro Vida", resp.nome());
        assertEquals(Categoria.VIDA, resp.categoria());
        Mockito.verify(service).searchProduct(id);
    }

    @Test
    void deveListarProdutos() {
        Product produto1 = new Product(UUID.randomUUID(), "Vida", Categoria.VIDA, new BigDecimal("100.00"), new BigDecimal("103.20"));
        Product produto2 = new Product(UUID.randomUUID(), "Auto", Categoria.AUTO, new BigDecimal("50.00"), new BigDecimal("55.25"));
        Mockito.when(service.listProduct()).thenReturn(List.of(produto1, produto2));

        List<ProductResponse> lista = controller.listProduct();

        assertEquals(2, lista.size());
        assertEquals("Vida", lista.get(0).nome());
        assertEquals("Auto", lista.get(1).nome());
        Mockito.verify(service).listProduct();
    }

    @Test
    void deveDeletarProduto() {
        UUID id = UUID.randomUUID();

        controller.deleteProduct(id);

        Mockito.verify(service).deleteProduct(id);
    }
}
