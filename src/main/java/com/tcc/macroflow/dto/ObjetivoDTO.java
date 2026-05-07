package com.tcc.macroflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcc.macroflow.model.TipoObjetivo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ObjetivoDTO {


    private Long tipoObjetivoId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFim;

}
