package br.com.seguros.tarifador.infrastructure.web.dto;

import br.com.seguros.tarifador.domain.model.Categoria;
import br.com.seguros.tarifador.infrastructure.persistence.ProductMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductRequestTest {

    private static Validator validator;
    private final ProductMapper mapper = new ProductMapper();

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void devePermitirValoresNulos() {
        ProductRequest req = new ProductRequest();
        assertNull(req.getNome());
        assertNull(req.getCategoria());
        assertNull(req.getPrecoBase());
        assertNull(req.getPrecoTarifado());
    }

    @Test
    void deveSetarERecuperarValoresCorretamente() {
        ProductRequest req = new ProductRequest();
        req.setNome("Seguro Auto");
        req.setCategoria(Categoria.AUTO);
        req.setPrecoBase(new BigDecimal("1000.00"));
        req.setPrecoTarifado(new BigDecimal("1100.00"));

        assertEquals("Seguro Auto", req.getNome());
        assertEquals(Categoria.AUTO, req.getCategoria());
        assertEquals(new BigDecimal("1000.00"), req.getPrecoBase());
        assertEquals(new BigDecimal("1100.00"), req.getPrecoTarifado());
    }

    @Test
    void deveRejeitarNomeNulo() {
        ProductRequest req = new ProductRequest();
        req.setNome(null);
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(new BigDecimal("100.00"));

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }

    @Test
    void deveRejeitarNomeVazio() {
        ProductRequest req = new ProductRequest();
        req.setNome("");
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(new BigDecimal("100.00"));

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }

    @Test
    void deveRejeitarNomeComApenasEspacos() {
        ProductRequest req = new ProductRequest();
        req.setNome("   ");
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(new BigDecimal("100.00"));

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }

    @Test
    void deveRejeitarNomeCurto() {
        ProductRequest req = new ProductRequest();
        req.setNome("AB");
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(new BigDecimal("100.00"));

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }

    @Test
    void deveRejeitarNomeLongo() {
        ProductRequest req = new ProductRequest();
        req.setNome("A".repeat(121));
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(new BigDecimal("100.00"));

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }

    @Test
    void deveAceitarNomeComTamanhoMinimo() {
        ProductRequest req = new ProductRequest();
        req.setNome("ABC");
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(new BigDecimal("100.00"));

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    void deveAceitarNomeComTamanhoMaximo() {
        ProductRequest req = new ProductRequest();
        req.setNome("A".repeat(120));
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(new BigDecimal("100.00"));

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    void deveRejeitarCategoriaNula() {
        ProductRequest req = new ProductRequest();
        req.setNome("Seguro Vida");
        req.setCategoria(null);
        req.setPrecoBase(new BigDecimal("100.00"));

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("categoria")));
    }

    @Test
    void deveRejeitarBasePriceNulo() {
        ProductRequest req = new ProductRequest();
        req.setNome("Seguro Vida");
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(null);

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty(), "Deveria rejeitar precoBase nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("precoBase")),
                "Deveria ter violação em basePrice");
    }

    @Test
    void deveRejeitarBasePriceZero() {
        ProductRequest req = new ProductRequest();
        req.setNome("Seguro Vida");
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(BigDecimal.ZERO);

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty(), "Deveria rejeitar precoBase zero");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("precoBase")),
                "Deveria ter violação em precoBase");
    }

    @Test
    void deveRejeitarBasePriceNegativo() {
        ProductRequest req = new ProductRequest();
        req.setNome("Seguro Vida");
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(new BigDecimal("-10.00"));

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty(), "Deveria rejeitar precoBase negativo");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("precoBase")),
                "Deveria ter violação em precoBase");
    }

    @Test
    void deveAceitarBasePriceNoLimiteMinimo() {
        ProductRequest req = new ProductRequest();
        req.setNome("Seguro Vida");
        req.setCategoria(Categoria.VIDA);
        req.setPrecoBase(new BigDecimal("0.01"));

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    void deveAceitarTodasAsCategorias() {
        for (Categoria cat : Categoria.values()) {
            ProductRequest req = new ProductRequest();
            req.setNome("Produto " + cat);
            req.setCategoria(cat);
            req.setPrecoBase(new BigDecimal("100.00"));

            Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
            assertTrue(violations.isEmpty());
        }
    }

    @Test
    void deveValidarQuandoRequestValido() {
        ProductRequest req = new ProductRequest();
        req.setNome("Seguro Auto Completo");
        req.setCategoria(Categoria.AUTO);
        req.setPrecoBase(new BigDecimal("1500.00"));
        req.setPrecoTarifado(new BigDecimal("1650.00"));

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    void toEntityDeveRetornarNullQuandoDomainForNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDomainDeveRetornarNullQuandoEntityForNull() {
        assertNull(mapper.toDomain(null));
    }

}
