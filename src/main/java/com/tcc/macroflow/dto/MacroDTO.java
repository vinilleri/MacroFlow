package com.tcc.macroflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MacroDTO {
    private BigDecimal calorias;
    private BigDecimal proteinas;
    private BigDecimal carboidrato;
    private BigDecimal gordura;

    public static MacroDTO somarDTO(MacroDTO atual, MacroDTO novo){
        BigDecimal totalCaloria = atual.calorias.add(novo.getCalorias());
        BigDecimal totalProteina = atual.proteinas.add(novo.getProteinas());
        BigDecimal totalCarboidrato = atual.carboidrato.add(novo.getCarboidrato());
        BigDecimal totalGordura = atual.gordura.add(novo.getGordura());

        return new MacroDTO(totalCaloria,totalProteina,totalCarboidrato,totalGordura);
    }
}
