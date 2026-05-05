package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.Icone;
import com.tcc.macroflow.enums.Origem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
