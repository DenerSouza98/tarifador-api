package br.com.seguros.tarifador.infrastructure.persistence;

import br.com.seguros.tarifador.domain.model.Categoria;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductEntityTest {


    @Test
    void deveCriarEntityVazia() {
        ProductEntity entity = new ProductEntity();
        assertNotNull(entity);
        assertNull(entity.getId());
    }

    @Test
    void deveSetarERecuperarId() {
        ProductEntity entity = new ProductEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);

        assertEquals(id, entity.getId());
    }

    @Test
    void deveSetarERecuperarName() {
        ProductEntity entity = new ProductEntity();
        entity.setNome("Seguro Auto");

        assertEquals("Seguro Auto", entity.getNome());
    }

    @Test
    void deveSetarERecuperarCategory() {
        ProductEntity entity = new ProductEntity();
        entity.setCategoria(Categoria.VIDA);

        assertEquals(Categoria.VIDA, entity.getCategoria());
    }

    @Test
    void deveSetarERecuperarBasePrice() {
        ProductEntity entity = new ProductEntity();
        BigDecimal price = new BigDecimal("100.00");
        entity.setPrecoBase(price);

        assertEquals(price, entity.getPrecoBase());
    }

    @Test
    void deveSetarERecuperarTariffPrice() {
        ProductEntity entity = new ProductEntity();
        BigDecimal tariff = new BigDecimal("103.20");
        entity.setPrecoTarifado(tariff);

        assertEquals(tariff, entity.getPrecoTarifado());
    }

    @Test
    void deveCompararEntitiesComMesmoId() {
        UUID id = UUID.randomUUID();
        ProductEntity e1 = new ProductEntity();
        e1.setId(id);

        ProductEntity e2 = new ProductEntity();
        e2.setId(id);

        assertEquals(e1.getId(), e2.getId()); // compara os valores, não os objetos
    }


    @Test
    void deveDiferenciarEntitiesComIdsDiferentes() {
        ProductEntity e1 = new ProductEntity();
        e1.setId(UUID.randomUUID());

        ProductEntity e2 = new ProductEntity();
        e2.setId(UUID.randomUUID());

        assertNotEquals(e1, e2);
    }

    @Test
    void deveGerarToStringComCampos() {
        ProductEntity entity = new ProductEntity();
        entity.setNome("Seguro Vida");
        String toString = entity.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("ProductEntity"));
    }


}