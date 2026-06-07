package com.tcc.macroflow.helper;


import com.tcc.macroflow.dto.ComidaResponseDTO;
import com.tcc.macroflow.dto.MacroDTO;
import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.model.Comida;
import com.tcc.macroflow.model.ComidaUsuario;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalcularQuantidade {
    public static BigDecimal calcularQuantidade(
            BigDecimal quantidade,
            BigDecimal valor,
            ComidaResponseDTO comida
    ){
        return quantidade.multiply(valor)
                .divide(comida.getValor(),4, RoundingMode.HALF_UP);
    }

    public static ComidaResponseDTO  converterComidaEmDto(Comida comida) {
        return new ComidaResponseDTO(comida.getId(),
                comida.getNome(),
                comida.getCalorias(),
                comida.getProteinas(),
                comida.getCarboidrato(),
                comida.getGordura(),
                comida.getIcone(),
                Origem.SISTEMA,
                comida.getUnidade().getId(),

                comida.getValor()
                );
    }
    public static ComidaResponseDTO  converterComidaUsuarioEmDto(ComidaUsuario comida) {
        return new ComidaResponseDTO(comida.getId(),
                comida.getNome(),
                comida.getCalorias(),
                comida.getProteinas(),
                comida.getCarboidrato(),
                comida.getGordura(),
                comida.getIcone(),
                Origem.USUARIO,
                comida.getUnidade().getId(),
                comida.getValor()
        );
    }

    public static MacroDTO calcularMacros(Comida comida, BigDecimal valor){


        BigDecimal totalCalorias = comida.getCalorias().multiply(valor.divide(comida.getValor(),
                2,RoundingMode.HALF_UP));
        BigDecimal totalProteinas = comida.getProteinas().multiply(valor.divide(comida.getValor(),
                2,RoundingMode.HALF_UP));
        BigDecimal totalCarboidrato = comida.getCarboidrato().multiply(valor.divide(comida.getValor(),
                2,RoundingMode.HALF_UP));
        BigDecimal totalGordura= comida.getGordura().multiply(valor.divide(comida.getValor(),
                2,RoundingMode.HALF_UP));

        return new MacroDTO(totalCalorias,totalProteinas,totalCarboidrato,totalGordura);
    }
    public static MacroDTO calcularMacros(ComidaUsuario comida, BigDecimal valor){

        BigDecimal fator = valor.divide(comida.getValor(),
                2, RoundingMode.HALF_UP);

        BigDecimal totalCalorias = comida.getCalorias().multiply(fator);
        BigDecimal totalProteinas = comida.getProteinas().multiply(fator);
        BigDecimal totalCarboidrato = comida.getCarboidrato().multiply(fator);
        BigDecimal totalGordura = comida.getGordura().multiply(fator);

        return new MacroDTO(
                totalCalorias,
                totalProteinas,
                totalCarboidrato,
                totalGordura
        );
    }



}
