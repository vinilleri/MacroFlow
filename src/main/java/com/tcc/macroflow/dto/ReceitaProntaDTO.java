package com.tcc.macroflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceitaProntaDTO {
    String nome;
    List<ReceitaProntaItemDTO> itemList;
}
