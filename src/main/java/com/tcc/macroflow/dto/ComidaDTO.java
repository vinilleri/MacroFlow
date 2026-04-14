package com.tcc.macroflow.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComidaDTO {

private String nome;
private BigDecimal calorias;
private BigDecimal proteinas;

private BigDecimal carboidrato;

private BigDecimal gordura;

}
