package br.com.seguros.tarifador.infrastructure.persistence;

import br.com.seguros.tarifador.domain.model.Categoria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SpringDataProductRepositoryTest {

    @Autowired
    private SpringDataProductRepository repository;

    @Test
    void deveSalvarERecuperarProduto() {
        ProductEntity entity = criarEntity("Seguro Vida", Categoria.VIDA);

        ProductEntity salvo = repository.save(entity);

        assertNotNull(salvo.getId());
        assertEquals("Seguro Vida", salvo.getNome());

        Optional<ProductEntity> recuperado = repository.findById(salvo.getId());
        assertTrue(recuperado.isPresent());
        assertEquals("Seguro Vida", recuperado.get().getNome());
    }

    @Test
    void deveListarTodosProdutos() {
        repository.save(criarEntity("Vida", Categoria.VIDA));
        repository.save(criarEntity("Auto", Categoria.AUTO));

        List<ProductEntity> lista = repository.findAll();

        assertTrue(lista.size() >= 2);
    }

    @Test
    void deveDeletarProduto() {
        ProductEntity salvo = repository.save(criarEntity("Temporário", Categoria.VIAGEM));
        UUID id = salvo.getId();

        repository.deleteById(id);

        Optional<ProductEntity> recuperado = repository.findById(id);
        assertFalse(recuperado.isPresent());
    }

    @Test
    void deveRetornarVazioQuandoIdNaoExiste() {
        UUID id = UUID.randomUUID();

        Optional<ProductEntity> result = repository.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void deveAtualizarProdutoExistente() {
        ProductEntity salvo = repository.save(criarEntity("Original", Categoria.AUTO));

        salvo.setNome("Atualizado");
        salvo.setCategoria(Categoria.VIDA);
        ProductEntity atualizado = repository.save(salvo);

        assertEquals("Atualizado", atualizado.getNome());
        assertEquals(Categoria.VIDA, atualizado.getCategoria());
    }

    private ProductEntity criarEntity(String name, Categoria category) {
        ProductEntity entity = new ProductEntity();
        entity.setNome(name);
        entity.setCategoria(category);
        entity.setPrecoBase(new BigDecimal("100.00"));
        entity.setPrecoTarifado(new BigDecimal("103.20"));
        return entity;
    }

}