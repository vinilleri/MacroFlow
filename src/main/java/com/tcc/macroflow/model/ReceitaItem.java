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
public class ReceitaItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comida_id", nullable = false)
    private Comida comida;

    @ManyToOne
    @JoinColumn(name = "receita_id", nullable = false)
    private Receita receita;
    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private BigDecimal quantidade;

}
