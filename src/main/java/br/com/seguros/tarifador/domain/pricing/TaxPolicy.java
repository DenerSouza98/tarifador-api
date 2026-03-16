package br.com.seguros.tarifador.domain.pricing;

import java.math.BigDecimal;

public interface TaxPolicy {
    BigDecimal calculate(BigDecimal precoBase);
}
