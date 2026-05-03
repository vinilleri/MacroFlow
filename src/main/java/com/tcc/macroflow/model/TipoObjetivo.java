package com.tcc.macroflow.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoObjetivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String tipo;
    @Column(nullable = false)
    private String descricao;
    @Column(nullable = false)
    private BigDecimal multiplicadorCalorico;
    @Column(nullable = false)
    private BigDecimal multiplicadorProteina;
    @Column(nullable = false)
    private BigDecimal multiplicadorGordura;



}
