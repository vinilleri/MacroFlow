package com.tcc.macroflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.enums.TipoConsumo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumoItemDTO {

    private Long idReceitaOuComida;
    private TipoConsumo tipoConsumo;
    private String nome;
    private BigDecimal quantidade;
    private BigDecimal valor;


}
