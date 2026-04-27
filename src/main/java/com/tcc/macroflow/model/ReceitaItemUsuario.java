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
public class ReceitaItemUsuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "comida_usuario_id", nullable = false)
        private ComidaUsuario comida;

        @ManyToOne
        @JoinColumn(name = "receita_id", nullable = false)
        private Receita receita;


        @ManyToOne
        @JoinColumn(name = "unidade_id", nullable = false)
        private Unidade unidade;

        @Column(nullable = false)
        private BigDecimal quantidade;
}
