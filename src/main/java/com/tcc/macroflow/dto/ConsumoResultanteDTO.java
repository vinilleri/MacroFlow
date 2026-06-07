package com.tcc.macroflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ConsumoResultanteDTO {

    private MacroDTO macroDTO;
    private BigDecimal quantidade;
}
