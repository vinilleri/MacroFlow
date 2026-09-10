package com.tcc.macroflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecuperacaoDTO {
    private String senha;
    private String codigo;
}
