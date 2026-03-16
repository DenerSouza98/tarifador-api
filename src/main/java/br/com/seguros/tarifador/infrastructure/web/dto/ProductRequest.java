package br.com.seguros.tarifador.infrastructure.web.dto;

import br.com.seguros.tarifador.domain.model.Categoria;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductRequest {

    @JsonProperty("nome")
    @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 3, max = 120, message = "Nome deve ter entre 3 e 120 caracteres")
    private String nome;

    @JsonProperty("categoria")
    @NotNull(message = "Categoria não pode ser nula")
    private Categoria categoria;

    @JsonProperty("preco_base")
    @NotNull(message = "Preço base não pode ser nulo")
    @DecimalMin(value = "0.01", inclusive = true, message = "Preço base deve ser maior ou igual a 0.01")
    private BigDecimal precoBase;

    @JsonProperty("preco_tarifado")
    private BigDecimal precoTarifado;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(BigDecimal precoBase) {
        this.precoBase = precoBase;
    }

    public BigDecimal getPrecoTarifado() {
        return precoTarifado;
    }

    public void setPrecoTarifado(BigDecimal precoTarifado) {
        this.precoTarifado = precoTarifado;
    }
}
