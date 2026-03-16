package br.com.seguros.tarifador.domain.pricing;

import br.com.seguros.tarifador.domain.model.Categoria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaxPolicyRegistry {
    public TaxPolicy getPolicy(Categoria categoria) {
        log.debug("Recuperando política de tarifação para categoria: {}", categoria);
        return new DefaultTaxPolicy(categoria);
    }
}
