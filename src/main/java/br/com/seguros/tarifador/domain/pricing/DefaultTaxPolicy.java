package br.com.seguros.tarifador.domain.pricing;

import br.com.seguros.tarifador.domain.model.Categoria;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class DefaultTaxPolicy implements TaxPolicy {
    private final Categoria categoria;

    public DefaultTaxPolicy(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public BigDecimal calculate(BigDecimal basePrice) {
        log.debug("Calculando tarifa para categoria={}, precoBase={}", categoria, basePrice);

        BigDecimal iof = basePrice.multiply(categoria.getIof());
        BigDecimal pis = basePrice.multiply(categoria.getPis());
        BigDecimal cofins = basePrice.multiply(categoria.getCofins());

        BigDecimal total = basePrice.add(iof).add(pis).add(cofins);
        BigDecimal resultado = total.setScale(2, RoundingMode.HALF_UP);

        log.debug("Tarifa calculada: iof={}, pis={}, cofins={}, total={}",
                iof, pis, cofins, resultado);

        return resultado;
    }
}
