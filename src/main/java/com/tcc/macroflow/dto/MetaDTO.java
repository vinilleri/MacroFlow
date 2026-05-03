package com.tcc.macroflow.dto;

import com.tcc.macroflow.enums.TipoMeta;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MetaDTO {

    private Long objetivoId;
    private TipoMeta tipoMeta;
    private LocalDate dataFim;
}
