package br.com.seguros.tarifador.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Product {
    private UUID id;

    @Setter
    private String nome;

    @Setter
    private Categoria categoria;

    @Setter
    private BigDecimal precoBase;

    @Setter
    private BigDecimal precoTarifado;


}
