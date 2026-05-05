package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.Icone;
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
    private Long unidadeId;
    private BigDecimal valor;

}
