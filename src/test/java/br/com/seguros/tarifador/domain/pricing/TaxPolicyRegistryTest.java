package br.com.seguros.tarifador.domain.pricing;

import br.com.seguros.tarifador.domain.model.Categoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaxPolicyRegistryTest {

    @Test
    void deveRetornarPolicyParaVida() {
        TaxPolicyRegistry registry = new TaxPolicyRegistry();
        TaxPolicy policy = registry.getPolicy(Categoria.VIDA);

        assertNotNull(policy);
        assertInstanceOf(DefaultTaxPolicy.class, policy);
    }

    @Test
    void deveRetornarPolicyParaAuto() {
        TaxPolicyRegistry registry = new TaxPolicyRegistry();
        TaxPolicy policy = registry.getPolicy(Categoria.AUTO);

        assertNotNull(policy);
        assertInstanceOf(DefaultTaxPolicy.class, policy);
    }

    @Test
    void deveRetornarPolicyDiferenteParaCadaCategoria() {
        TaxPolicyRegistry registry = new TaxPolicyRegistry();

        TaxPolicy vidaPolicy = registry.getPolicy(Categoria.VIDA);
        TaxPolicy autoPolicy = registry.getPolicy(Categoria.AUTO);

        assertNotNull(vidaPolicy);
        assertNotNull(autoPolicy);
    }

}