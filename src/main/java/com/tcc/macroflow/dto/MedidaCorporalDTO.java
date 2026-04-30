package com.tcc.macroflow.dto;

import com.tcc.macroflow.model.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MedidaCorporalDTO {


    private BigDecimal peso;

    private Integer altura;

    private BigDecimal percentualGordura;

    private BigDecimal circuferenciaCintura;

    private LocalDate data;
}
