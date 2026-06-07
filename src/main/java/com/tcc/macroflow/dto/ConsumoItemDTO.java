package com.tcc.macroflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.enums.TipoConsumo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ConsumoItemDTO {

    private Long idReceitaOuComida;
    private TipoConsumo tipoConsumo;
    private String nome;
    private BigDecimal quantidade;
    private BigDecimal valor;


}
