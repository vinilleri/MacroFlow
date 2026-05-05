package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.Origem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceitaItemRequestDTO {
    private  Long comidaId;
    private Long unidadeId;
    private BigDecimal quantidade;
    private BigDecimal valor;
    private Origem origem;
}

