package br.com.seguros.tarifador.infrastructure.web.dto;

import br.com.seguros.tarifador.domain.model.Categoria;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String nome,
        Categoria categoria,
        BigDecimal precoBase,
        BigDecimal precoTarifado
) {}
