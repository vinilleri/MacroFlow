package com.tcc.macroflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcc.macroflow.model.TipoObjetivo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@AllArgsConstructor
@Data
public class ObjetivoResponseDTO {
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFinal;
    private TipoObjetivo tipoObjetivo;

}
