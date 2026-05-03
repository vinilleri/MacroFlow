package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.Icone;
import com.tcc.macroflow.enums.Origem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComidaDTO {
private Long id;
private String nome;
private BigDecimal calorias;
private BigDecimal proteinas;
private BigDecimal carboidrato;
private BigDecimal gordura;
private Icone icone;
private Long unidadeId;
private Origem origem;
private BigDecimal valor;
}
