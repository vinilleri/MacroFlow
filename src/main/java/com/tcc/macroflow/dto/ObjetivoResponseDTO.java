package com.tcc.macroflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
public class ObjetivoResponseDTO {
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;

    private Long tipoObjetivoId;

}
