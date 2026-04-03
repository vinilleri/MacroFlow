package com.tcc.macroflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String senha;
    @Column(nullable = false)
    private boolean ativo = true;
    @Column(nullable = false)
    private boolean verificado = true;

    @OneToOne
    @JoinColumn(name = "atividade_fisica_id", nullable = false)
    private AtividadeFisica atividadeFisica;
}
