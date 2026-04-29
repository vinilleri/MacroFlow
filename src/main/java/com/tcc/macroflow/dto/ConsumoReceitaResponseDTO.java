package com.tcc.macroflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ConsumoReceitaResponseDTO {
    private Long consumoId;

    private Long receitaId;

    private BigDecimal quantidade;

}
