package com.tcc.macroflow.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsumoReceitaDTO {

    private Long receitaId;
    private BigDecimal quantidade;
}
