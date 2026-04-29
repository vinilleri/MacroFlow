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
public class ConsumoComidaUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consumo_id", nullable = false)
    private Consumo consumo;

    @ManyToOne
    @JoinColumn(name = "comida_usuario_id", nullable = false)
    private ComidaUsuario comidaUsuario;

    private BigDecimal quantidade;



}
