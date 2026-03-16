package br.com.seguros.tarifador.infrastructure.persistence;

import br.com.seguros.tarifador.application.ports.ProductRepository;
import br.com.seguros.tarifador.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryAdapter implements ProductRepository {

    private final SpringDataProductRepository repo;
    private final ProductMapper mapper;

    public Product save(Product product) {
        log.debug("Salvando produto no banco: id={}", product.getId());
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity saved = repo.save(entity);
        log.debug("Produto salvo com sucesso: id={}", saved.getId());
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        log.debug("Buscando produto por id={}", id);
        return repo.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        log.debug("Listando todos os produtos");
        return repo.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deletando produto: id={}", id);
        repo.deleteById(id);
    }
}
