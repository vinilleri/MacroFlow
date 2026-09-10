package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.enums.TipoComidaReceita;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlimentoRecomendadoDTO {

    String nome;
    Long id;
    BigDecimal calorias;
    TipoComidaReceita tipo;
    BigDecimal proteinas;
    BigDecimal carboidrato;
    BigDecimal gordura;
    BigDecimal score;

}
