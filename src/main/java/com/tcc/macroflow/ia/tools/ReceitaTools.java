package com.tcc.macroflow.ia.tools;

import com.tcc.macroflow.dto.ReceitaProntaDTO;
import com.tcc.macroflow.service.ReceitaService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ReceitaTools {
    private final ReceitaService receitaService;

    public ReceitaTools(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    @Tool(description = "Cria uma receita com seus ingredientes.")
    public void criarReceita(ReceitaProntaDTO receitaProntaDTO){
        receitaService.criarReceitaPronta(receitaProntaDTO);
    }
}
