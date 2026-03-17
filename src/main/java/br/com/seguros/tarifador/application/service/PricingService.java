package br.com.seguros.tarifador.application.service;

import br.com.seguros.tarifador.application.ports.ProductRepository;
import br.com.seguros.tarifador.domain.model.Product;
import br.com.seguros.tarifador.domain.pricing.TaxPolicyRegistry;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PricingService {

    private final ProductRepository repository;
    private final TaxPolicyRegistry registry;
    private final Counter pricingErrors;

    public PricingService(ProductRepository repository, TaxPolicyRegistry registry, MeterRegistry meterRegistry) {
        this.repository = repository;
        this.registry = registry;
        this.pricingErrors = Counter.builder("pricing_errors_total")
                .description("Total de erros no cálculo de preços")
                .register(meterRegistry);
    }

    @Timed(value = "pricing.create", description = "Tempo gasto para criar um produto")
    @NewSpan("createProduct")
    public Product createProduct(Product newProduct) {
        log.info("Criando produto: categoria={}, precoBase={}",
                newProduct.getCategoria(), newProduct.getPrecoBase());

        try {
            BigDecimal pt = registry.getPolicy(newProduct.getCategoria())
                    .calculate(newProduct.getPrecoBase());
            newProduct.setPrecoTarifado(pt);
            Product saved = repository.save(newProduct);

            log.info("Produto criado com sucesso: id={}, precoTarifado={}",
                    saved.getId(), saved.getPrecoTarifado());
            return saved;
        } catch (Exception e) {
            pricingErrors.increment();
            log.error("Erro ao criar produto: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Timed(value = "pricing.update", description = "Tempo gasto para atualizar um produto")
    @NewSpan("updateProduct")
    public Product updateProduct(UUID id, Product updates) {
        log.info("Atualizando produto: id={}", id);

        try {
            Product current = repository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Produto não encontrado: id={}", id);
                        return new EntityNotFoundException("Produto não encontrado: " + id);
                    });

            current.setNome(updates.getNome());
            current.setCategoria(updates.getCategoria());
            current.setPrecoBase(updates.getPrecoBase());

            BigDecimal pt = registry.getPolicy(current.getCategoria())
                    .calculate(current.getPrecoBase());
            current.setPrecoTarifado(pt);

            Product saved = repository.save(current);
            log.info("Produto atualizado: id={}, novoPrecoTarifado={}",
                    saved.getId(), saved.getPrecoTarifado());
            return saved;
        } catch (Exception e) {
            pricingErrors.increment();
            log.error("Erro ao atualizar produto: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Timed(value = "pricing.delete", description = "Tempo gasto para deletar um produto")
    @NewSpan("deleteProduct")
    public void deleteProduct(UUID id) {
        log.info("Deletando produto: id={}", id);
        try {
            repository.deleteById(id);
            log.info("Produto deletado com sucesso: id={}", id);
        } catch (Exception e) {
            pricingErrors.increment();
            log.error("Erro ao deletar produto: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Timed(value = "pricing.search", description = "Tempo gasto para buscar um produto")
    @NewSpan("searchProduct")
    public Product searchProduct(UUID id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));
        } catch (Exception e) {
            pricingErrors.increment();
            log.error("Erro ao buscar produto: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Timed(value = "pricing.list", description = "Tempo gasto para listar produtos")
    @NewSpan("listProduct")
    public List<Product> listProduct() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            pricingErrors.increment();
            log.error("Erro ao listar produtos: {}", e.getMessage(), e);
            throw e;
        }
    }

}
