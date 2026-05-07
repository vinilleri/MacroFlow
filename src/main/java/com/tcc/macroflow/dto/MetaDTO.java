package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.TipoMeta;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MetaDTO {

    private Long objetivoId;
    private TipoMeta tipoMeta;
    private BigDecimal calorias;
    private BigDecimal proteinas;
    private BigDecimal carboidrato;
    private BigDecimal gordura;
}
