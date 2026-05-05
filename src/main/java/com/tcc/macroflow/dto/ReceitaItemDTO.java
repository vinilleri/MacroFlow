package com.tcc.macroflow.dto;


import com.tcc.macroflow.enums.Origem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ReceitaItemDTO {


    private BigDecimal calorias;

    private String nome;

    private BigDecimal quantidade;

    private BigDecimal valor;
    private Origem origem;

}
