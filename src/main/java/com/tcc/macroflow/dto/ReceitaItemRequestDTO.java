package com.tcc.macroflow.dto;

import com.tcc.macroflow.component.Origem;
import lombok.Data;

@Data
public class ReceitaItemRequestDTO {
    private  Long comidaId;
    private Long unidadeId;
    private Integer quantidade;
    private Origem origem;
}

