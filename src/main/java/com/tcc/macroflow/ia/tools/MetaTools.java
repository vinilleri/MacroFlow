package com.tcc.macroflow.ia.tools;

import com.tcc.macroflow.dto.MetaResponseDTO;
import com.tcc.macroflow.service.MetaService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class MetaTools {
    private final MetaService metaService;

    public MetaTools(MetaService metaService) {
        this.metaService = metaService;
    }

    @Tool(description = "Consulta a meta nutricional atual do usuário.")
    public MetaResponseDTO buscarMetaAtual(){
        return metaService.buscarMetaAtual();
    }
}
