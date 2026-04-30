package com.tcc.macroflow.dto;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
public class MedidaCorporalResponseDTO {
    private BigDecimal peso;

    private Integer altura;

    private BigDecimal percentualGordura;

    private BigDecimal circuferenciaCintura;

    private LocalDate data;
}
