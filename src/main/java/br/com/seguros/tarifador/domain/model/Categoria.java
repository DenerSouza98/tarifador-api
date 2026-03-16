package br.com.seguros.tarifador.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum Categoria {
    VIDA(new BigDecimal("0.01"),  new BigDecimal("0.022"), BigDecimal.ZERO),
    AUTO(new BigDecimal("0.055"), new BigDecimal("0.04"),  new BigDecimal("0.01")),
    VIAGEM(new BigDecimal("0.02"), new BigDecimal("0.04"), new BigDecimal("0.01")),
    RESIDENCIAL(new BigDecimal("0.04"), BigDecimal.ZERO,   new BigDecimal("0.03")),
    PATRIMONIAL(new BigDecimal("0.05"), new BigDecimal("0.03"), BigDecimal.ZERO);

    private final BigDecimal iof;
    private final BigDecimal pis;
    private final BigDecimal cofins;

}
