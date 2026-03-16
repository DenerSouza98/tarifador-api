package br.com.seguros.tarifador.infrastructure.web.dto;

import br.com.seguros.tarifador.domain.model.Categoria;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String nome,
        Categoria categoria,
        BigDecimal precoBase,
        BigDecimal precoTarifado
) {}
