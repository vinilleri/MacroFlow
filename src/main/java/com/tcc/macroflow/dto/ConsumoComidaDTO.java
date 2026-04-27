package com.tcc.macroflow.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ConsumoComidaDTO {


    private Long comidaId;
    private Long unidadeId;
    private BigDecimal quantidade;
}
