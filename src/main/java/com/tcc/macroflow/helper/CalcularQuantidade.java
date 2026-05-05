package com.tcc.macroflow.helper;

import com.tcc.macroflow.dto.ComidaDTO;
import com.tcc.macroflow.dto.ComidaResponseDTO;
import com.tcc.macroflow.model.Comida;
import com.tcc.macroflow.model.ComidaUsuario;
import com.tcc.macroflow.model.Unidade;

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
                comida.getUnidade().getId(),
                comida.getValor()
        );
    }


}
