package br.com.seguros.tarifador.infrastructure.persistence;

import br.com.seguros.tarifador.domain.model.Categoria;
import br.com.seguros.tarifador.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class ProductRepositoryAdapterTest {

    private SpringDataProductRepository repo;
    private ProductMapper mapper;
    private ProductRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(SpringDataProductRepository.class);
        mapper = new ProductMapper();
        adapter = new ProductRepositoryAdapter(repo, mapper);
    }

    @Test
    void deveSalvarProduto() {
        ProductEntity entity = new ProductEntity();
        entity.setId(UUID.randomUUID());
        entity.setNome("Seguro Vida");
        entity.setCategoria(Categoria.VIDA);
        entity.setPrecoBase(new BigDecimal("100.00"));
        entity.setPrecoTarifado(new BigDecimal("103.20"));

        Mockito.when(repo.save(any())).thenReturn(entity);

        Product p = new Product(null, "Seguro Vida", Categoria.VIDA,
                new BigDecimal("100.00"), new BigDecimal("103.20"));
        Product salvo = adapter.save(p);

        assertEquals("Seguro Vida", salvo.getNome());
        assertEquals(Categoria.VIDA, salvo.getCategoria());
    }

    @Test
    void deveBuscarProdutoPorId() {
        UUID id = UUID.randomUUID();
        ProductEntity entity = new ProductEntity();
        entity.setId(id);
        entity.setNome("Auto");
        entity.setCategoria(Categoria.AUTO);
        entity.setPrecoBase(new BigDecimal("50.00"));
        entity.setPrecoTarifado(new BigDecimal("55.25"));

        Mockito.when(repo.findById(id)).thenReturn(Optional.of(entity));

        Optional<Product> result = adapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals("Auto", result.get().getNome());
    }

    @Test
    void deveRetornarListaDeProdutos() {
        ProductEntity e1 = new ProductEntity();
        e1.setId(UUID.randomUUID());
        e1.setNome("Vida");
        e1.setCategoria(Categoria.VIDA);
        e1.setPrecoBase(new BigDecimal("100.00"));
        e1.setPrecoTarifado(new BigDecimal("103.20"));

        ProductEntity e2 = new ProductEntity();
        e2.setId(UUID.randomUUID());
        e2.setNome("Auto");
        e2.setCategoria(Categoria.AUTO);
        e2.setPrecoBase(new BigDecimal("50.00"));
        e2.setPrecoTarifado(new BigDecimal("55.25"));

        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<Product> lista = adapter.findAll();

        assertEquals(2, lista.size());
        assertEquals("Vida", lista.get(0).getNome());
    }

    @Test
    void deveDeletarProdutoPorId() {
        UUID id = UUID.randomUUID();

        adapter.deleteById(id);

        Mockito.verify(repo).deleteById(id);
    }
}
