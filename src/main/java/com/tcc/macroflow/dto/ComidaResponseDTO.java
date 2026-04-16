package com.tcc.macroflow.dto;

import com.tcc.macroflow.component.Icone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComidaResponseDTO {
    private Long id;
    private String nome;
    private BigDecimal calorias;
    private BigDecimal proteinas;
    private BigDecimal carboidrato;
    private BigDecimal gordura;
    private Icone icone;

}
