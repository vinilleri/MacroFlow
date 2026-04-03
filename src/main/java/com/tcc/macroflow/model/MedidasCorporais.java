package com.tcc.macroflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedidasCorporais {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @Column(nullable = false)
    private BigDecimal peso;

    @Column(nullable = false)
    private Integer altura;

    @Column(nullable = false)
    private BigDecimal percentualGordura;

    @Column(nullable = false)
    private BigDecimal circuferenciaCintura;

    @Column(nullable = false)
    private LocalDate  data;

}
