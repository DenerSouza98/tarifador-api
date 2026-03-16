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

}