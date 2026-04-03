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

public class ConsumoReceita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consumo_id", nullable = false)
    private Consumo consumo;

    @ManyToOne
    @JoinColumn(name = "receita_id", nullable = false)
    private Receita receita;

    @Column(nullable = false)
    private BigDecimal quantidade;
}
