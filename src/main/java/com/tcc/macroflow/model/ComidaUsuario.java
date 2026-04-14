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
public class ComidaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private BigDecimal calorias;
    @Column(nullable = false)
    private BigDecimal proteinas;
    @Column(nullable = false)
    private BigDecimal carboidrato;
    @Column(nullable = false)
    private BigDecimal gordura;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
