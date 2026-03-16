package br.com.seguros.tarifador.infrastructure.web.dto;

import br.com.seguros.tarifador.domain.model.Categoria;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductResponseTest {

    @Test
    void deveCriarResponseComTodosOsCampos() {
        UUID id = UUID.randomUUID();
        String name = "Seguro Auto";
        Categoria category = Categoria.AUTO;
        BigDecimal basePrice = new BigDecimal("100.00");
        BigDecimal tariffPrice = new BigDecimal("105.50");

        ProductResponse response = new ProductResponse(id, name, category, basePrice, tariffPrice);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals(name, response.nome());
        assertEquals(category, response.categoria());
        assertEquals(basePrice, response.precoBase());
        assertEquals(tariffPrice, response.precoTarifado());
    }

    @Test
    void deveRecuperarTodosOsValoresPelosGetters() {
        UUID id = UUID.randomUUID();
        ProductResponse response = new ProductResponse(
                id,
                "Seguro Vida",
                Categoria.VIDA,
                new BigDecimal("50.00"),
                new BigDecimal("51.60")
        );

        assertEquals(id, response.id());
        assertEquals("Seguro Vida", response.nome());
        assertEquals(Categoria.VIDA, response.categoria());
        assertEquals(new BigDecimal("50.00"), response.precoBase());
        assertEquals(new BigDecimal("51.60"), response.precoTarifado());
    }

    @Test
    void devePermitirValoresNulos() {
        ProductResponse response = new ProductResponse(null, null, null, null, null);

        assertNull(response.id());
        assertNull(response.nome());
        assertNull(response.categoria());
        assertNull(response.precoBase());
        assertNull(response.precoTarifado());
    }
}
