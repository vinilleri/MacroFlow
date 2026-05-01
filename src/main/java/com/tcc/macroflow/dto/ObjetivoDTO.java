package com.tcc.macroflow.dto;

import com.tcc.macroflow.model.TipoObjetivo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ObjetivoDTO {


    private Long tipoObjetivoId;

    private LocalDate dataFim;

}
