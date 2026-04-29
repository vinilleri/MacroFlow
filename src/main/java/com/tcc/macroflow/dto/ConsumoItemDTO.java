package com.tcc.macroflow.dto;

import com.tcc.macroflow.component.TipoConsumo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ConsumoItemDTO {

    private Long consumoId;
    private TipoConsumo tipoConsumo;
    private String nome;
    private BigDecimal quantidade;
    private Long unidadeId;
    private BigDecimal calorias;
    private BigDecimal proteinas;
    private BigDecimal carboidrato;
    private BigDecimal gordura;
    private LocalDateTime dataHora;
}
