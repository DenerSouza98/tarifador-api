package br.com.seguros.tarifador.infrastructure.persistence;

import br.com.seguros.tarifador.domain.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity toEntity(Product domain) {
        if (domain == null) return null;

        ProductEntity entity = new ProductEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setCategoria(domain.getCategoria());
        entity.setPrecoBase(domain.getPrecoBase());
        entity.setPrecoTarifado(domain.getPrecoTarifado());
        return entity;
    }

    public Product toDomain(ProductEntity entity) {
        if (entity == null) return null;

        return new Product(
                entity.getId(),
                entity.getNome(),
                entity.getCategoria(),
                entity.getPrecoBase(),
                entity.getPrecoTarifado()
        );
    }
}
