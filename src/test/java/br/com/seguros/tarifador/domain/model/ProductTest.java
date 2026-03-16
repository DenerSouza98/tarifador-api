package br.com.seguros.tarifador.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {


    @Test
    void deveCriarProdutoComTodosCampos() {
        UUID id = UUID.randomUUID();
        String name = "Seguro Auto";
        Categoria category = Categoria.AUTO;
        BigDecimal basePrice = new BigDecimal("100.00");
        BigDecimal tariffPrice = new BigDecimal("110.50");

        Product product = new Product(id, name, category, basePrice, tariffPrice);

        assertEquals(id, product.getId());
        assertEquals(name, product.getNome());
        assertEquals(category, product.getCategoria());
        assertEquals(basePrice, product.getPrecoBase());
        assertEquals(tariffPrice, product.getPrecoTarifado());
    }

    @Test
    void devePermitirSetTariffPrice() {
        Product product = new Product(null, "Vida", Categoria.VIDA, new BigDecimal("100.00"), null);
        product.setPrecoTarifado(new BigDecimal("103.20"));

        assertEquals(new BigDecimal("103.20"), product.getPrecoTarifado());
    }

    @Test
    void devePermitirAlterarNome() {
        Product product = new Product(null, "Vida", Categoria.VIDA, new BigDecimal("100.00"), null);
        product.setNome("Seguro Vida Atualizado");

        assertEquals("Seguro Vida Atualizado", product.getNome());
    }

    @Test
    void devePermitirAlterarCategoria() {
        Product product = new Product(null, "Vida", Categoria.VIDA, new BigDecimal("100.00"), null);
        product.setCategoria(Categoria.AUTO);

        assertEquals(Categoria.AUTO, product.getCategoria());
    }

    @Test
    void devePermitirAlterarBasePrice() {
        Product product = new Product(null, "Vida", Categoria.VIDA, new BigDecimal("100.00"), null);
        product.setPrecoBase(new BigDecimal("200.00"));

        assertEquals(new BigDecimal("200.00"), product.getPrecoBase());
    }

}