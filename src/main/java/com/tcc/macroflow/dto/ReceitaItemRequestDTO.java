package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.Origem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceitaItemRequestDTO {
    private  Long comidaId;
    private Long unidadeId;
    private BigDecimal quantidade;
    private BigDecimal valor;
    private Origem origem;
}

