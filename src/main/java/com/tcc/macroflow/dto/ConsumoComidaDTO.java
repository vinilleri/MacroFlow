package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.Origem;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ConsumoComidaDTO {


    private Long comidaId;
    private BigDecimal quantidade;
    private Origem origem;
}
