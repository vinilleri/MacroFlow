package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.TipoMeta;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
public class MetaResponseDTO {
    private BigDecimal calorias;
    private BigDecimal proteinas;
    private BigDecimal carboidrato;
    private BigDecimal gordura;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private Long objetivoId;

    private TipoMeta tipoMeta;

}
