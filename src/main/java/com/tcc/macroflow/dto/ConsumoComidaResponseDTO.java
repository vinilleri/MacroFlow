package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.Origem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ConsumoComidaResponseDTO {
 private Long id;
    private Long consumoId;

    private Long comidaId;

    private BigDecimal quantidade;

    private Origem origem;

}
