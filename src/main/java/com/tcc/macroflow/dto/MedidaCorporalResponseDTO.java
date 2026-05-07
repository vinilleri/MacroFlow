package com.tcc.macroflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    private Sexo sexo;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;
}
