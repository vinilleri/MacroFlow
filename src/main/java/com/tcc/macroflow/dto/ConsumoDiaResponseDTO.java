package com.tcc.macroflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ConsumoDiaResponseDTO {

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataHora;
    private BigDecimal totalCalorias;
    private BigDecimal totalProteina;
    private BigDecimal totalCarboidrato;
    private BigDecimal totalGordura;
    private List<ConsumoItemDTO> itens;
}
