package com.tcc.macroflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcc.macroflow.enums.TipoMeta;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MetaDTO {

    private Long objetivoId;
    private TipoMeta tipoMeta;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFim;
    private BigDecimal calorias;
    private BigDecimal proteinas;
    private BigDecimal carboidrato;
    private BigDecimal gordura;
}
