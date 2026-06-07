package com.tcc.macroflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor



public class Consumo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal quantidade;


    @Column(nullable = false)
    private BigDecimal calorias;
    @Column(nullable = false)
    private BigDecimal proteinas;
    @Column(nullable = false)
    private BigDecimal carboidrato;
    @Column(nullable = false)
    private BigDecimal gordura;



}
