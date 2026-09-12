package com.tcc.macroflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceitaProntaItemDTO {
        private String nomeComida;
        private BigDecimal quantidade;
        private BigDecimal valor;

}
