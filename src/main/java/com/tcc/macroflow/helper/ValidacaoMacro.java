package com.tcc.macroflow.helper;

import com.tcc.macroflow.dto.MetaDTO;

import java.math.BigDecimal;

public class ValidacaoMacro {

    public static void validarMacro(MetaDTO dto){


        BigDecimal calorias = dto.getCalorias();
        BigDecimal proteinas = dto.getProteinas();
        BigDecimal carboidrato = dto.getCarboidrato();
        BigDecimal gordura = dto.getGordura();
        if(calorias == null || calorias.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Calorias inválidas");
        }
        if(proteinas == null || proteinas.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Proteinas inválidas");
        }
        if(carboidrato == null || carboidrato.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Carboidratos inválidas");
        }
        if(gordura == null || gordura.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Gorduras inválidas");
        }

        BigDecimal caloriasCalculadas = proteinas.multiply(BigDecimal.valueOf(4))
                .add(carboidrato.multiply(BigDecimal.valueOf(4)))
                .add(gordura.multiply(BigDecimal.valueOf(9)));


        BigDecimal tolerancia = BigDecimal.TEN;

        if(calorias.subtract(caloriasCalculadas).abs().compareTo(tolerancia) >0){
            throw  new RuntimeException("Configuração de dieta inválida");
        }

    }
}
