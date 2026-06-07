package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.Origem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReceitaItemResponseDTO {
    private Long id;
    private BigDecimal calorias;
    private BigDecimal proteinas;
    private BigDecimal carboidrato;
    private BigDecimal gordura;



    private String nome;

    private BigDecimal quantidade;

    private BigDecimal valor;



    private Origem origem;

}
