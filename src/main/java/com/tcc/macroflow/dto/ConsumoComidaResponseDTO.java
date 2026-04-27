package com.tcc.macroflow.dto;

import com.tcc.macroflow.model.Comida;
import com.tcc.macroflow.model.Consumo;
import com.tcc.macroflow.model.Unidade;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ConsumoComidaResponseDTO {

    private Long consumoId;

    private Long comidaId;

    private BigDecimal quantidade;

    private Long unidadeId;
}
