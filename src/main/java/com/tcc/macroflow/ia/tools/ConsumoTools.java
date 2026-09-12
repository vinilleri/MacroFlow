package com.tcc.macroflow.ia.tools;

import com.tcc.macroflow.dto.MacroDTO;
import com.tcc.macroflow.service.ConsumoService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ConsumoTools {

    private final ConsumoService consumoService;

    public ConsumoTools(ConsumoService consumoService) {
        this.consumoService = consumoService;
    }
    @Tool(description = "Consulta o consumo nutricional do usuário no dia atual.")
    public MacroDTO chamarConsumoDia(){
        return  consumoService.somarConsumoDia();
    }
}
