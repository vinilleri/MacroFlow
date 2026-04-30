package com.tcc.macroflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ConsumoComidaResponseDTO {

    private Long consumoId;

    private Long comidaId;

    private BigDecimal quantidade;

}
