package br.com.seguros.tarifador.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {


    @Test
    void deveRetornarValoresCorretosParaVida() {
        Categoria c = Categoria.VIDA;
        assertEquals(new BigDecimal("0.01"), c.getIof());
        assertEquals(new BigDecimal("0.022"), c.getPis());
        assertEquals(BigDecimal.ZERO, c.getCofins());
    }

    @Test
    void deveRetornarValoresCorretosParaAuto() {
        Categoria c = Categoria.AUTO;
        assertEquals(new BigDecimal("0.055"), c.getIof());
        assertEquals(new BigDecimal("0.04"), c.getPis());
        assertEquals(new BigDecimal("0.01"), c.getCofins());
    }

    @Test
    void deveRetornarValoresCorretosParaViagem() {
        Categoria c = Categoria.VIAGEM;
        assertEquals(new BigDecimal("0.02"), c.getIof());
        assertEquals(new BigDecimal("0.04"), c.getPis());
        assertEquals(new BigDecimal("0.01"), c.getCofins());
    }

    @Test
    void deveRetornarValoresCorretosParaResidencial() {
        Categoria c = Categoria.RESIDENCIAL;
        assertEquals(new BigDecimal("0.04"), c.getIof());
        assertEquals(BigDecimal.ZERO, c.getPis());
        assertEquals(new BigDecimal("0.03"), c.getCofins());
    }

    @Test
    void deveRetornarValoresCorretosParaPatrimonial() {
        Categoria c = Categoria.PATRIMONIAL;
        assertEquals(new BigDecimal("0.05"), c.getIof());
        assertEquals(new BigDecimal("0.03"), c.getPis());
        assertEquals(BigDecimal.ZERO, c.getCofins());
    }

    @Test
    void deveConterTodosOsValoresEnum() {
        Categoria[] values = Categoria.values();
        assertEquals(5, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(Categoria.VIDA));
        assertTrue(java.util.Arrays.asList(values).contains(Categoria.AUTO));
        assertTrue(java.util.Arrays.asList(values).contains(Categoria.VIAGEM));
        assertTrue(java.util.Arrays.asList(values).contains(Categoria.RESIDENCIAL));
        assertTrue(java.util.Arrays.asList(values).contains(Categoria.PATRIMONIAL));
    }

}