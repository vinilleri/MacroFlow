package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedidaCorporalResponseDTO {
    private BigDecimal peso;

    private Integer altura;
    private BigDecimal percentualGordura;

    private BigDecimal circuferenciaCintura;

    private LocalDate dataNascimento;
    private Sexo sexo;

    private LocalDate data;
}
