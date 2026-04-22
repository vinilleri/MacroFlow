package com.tcc.macroflow.dto;


import com.tcc.macroflow.component.Origem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ReceitaItemDTO {


    private BigDecimal calorias;

    private String nome;

    private String unidade;

    private Integer quantidade = 0;

    private Origem origem;

}
