package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.TipoMeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaRequisicaoDTO {
    private TipoMeta tipoMeta;
    private BigDecimal calorias;
    private BigDecimal proteinas;
    private BigDecimal carboidrato;
    private BigDecimal gordura;
}
