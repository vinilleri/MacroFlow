package com.tcc.macroflow.dto;

import com.tcc.macroflow.component.Icone;
import com.tcc.macroflow.component.Origem;
import com.tcc.macroflow.model.Unidade;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
