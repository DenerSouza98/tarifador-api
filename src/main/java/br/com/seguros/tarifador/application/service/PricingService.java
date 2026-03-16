package br.com.seguros.tarifador.application.service;

import br.com.seguros.tarifador.application.ports.ProductRepository;
import br.com.seguros.tarifador.domain.model.Product;
import br.com.seguros.tarifador.domain.pricing.TaxPolicyRegistry;
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

    public PricingService(ProductRepository repository, TaxPolicyRegistry registry) {
        this.repository = repository;
        this.registry = registry;
    }

    public Product createProduct(Product newProduct) {
        log.info("Criando produto: categoria={}, precoBase={}",
                newProduct.getCategoria(), newProduct.getPrecoBase());

        BigDecimal pt = registry.getPolicy(newProduct.getCategoria())
                .calculate(newProduct.getPrecoBase());
        newProduct.setPrecoTarifado(pt);
        Product saved = repository.save(newProduct);

        log.info("Produto criado com sucesso: id={}, precoTarifado={}",
                saved.getId(), saved.getPrecoTarifado());
        return saved;
    }

    public Product updateProduct(UUID id, Product updates) {
        log.info("Atualizando produto: id={}", id);

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
    }

    public void deleteProduct(UUID id) {
        log.info("Deletando produto: id={}", id);
        repository.deleteById(id);
        log.info("Produto deletado com sucesso: id={}", id);
    }

    public Product searchProduct(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));
    }

    public List<Product> listProduct() {
        return repository.findAll();
    }

}
