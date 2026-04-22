package com.tcc.macroflow.dto;

import com.tcc.macroflow.component.Icone;
import com.tcc.macroflow.component.Origem;
import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComidaDTO {
private Long id;
private String nome;
private BigDecimal calorias;
private BigDecimal proteinas;
private BigDecimal carboidrato;
private Icone icone;
private BigDecimal gordura;
private Origem origem;
}
