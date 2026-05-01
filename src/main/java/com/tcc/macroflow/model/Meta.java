package com.tcc.macroflow.model;

import com.tcc.macroflow.component.TipoMeta;
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
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario.id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private BigDecimal calorias;
    @Column(nullable = false)
    private BigDecimal proteinas;
    @Column(nullable = false)
    private BigDecimal carboidrato;
    @Column(nullable = false)
    private BigDecimal gordura;

    @Column(nullable = false)
    private LocalDate dataInicio;
    @Column(nullable = false)
    private LocalDate dataFim;

    @Column(nullable = false)
    private TipoMeta tipoMeta;


}
