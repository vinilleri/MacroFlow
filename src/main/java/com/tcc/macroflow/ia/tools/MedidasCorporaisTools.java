package com.tcc.macroflow.ia.tools;

import com.tcc.macroflow.dto.MedidaCorporalResponseDTO;
import com.tcc.macroflow.model.AtividadeFisica;
import com.tcc.macroflow.service.MedidasCorporaisService;
import com.tcc.macroflow.service.UsuarioService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class MedidasCorporaisTools {
    private final MedidasCorporaisService medidasCorporaisService;
    private final UsuarioService usuarioService;
    public MedidasCorporaisTools(MedidasCorporaisService medidasCorporaisService, UsuarioService usuarioService) {
        this.medidasCorporaisService = medidasCorporaisService;
        this.usuarioService = usuarioService;
    }

    @Tool(description = "Achar as medidas corporais do usuário")
    public MedidaCorporalResponseDTO buscarMedida(){
        return medidasCorporaisService.buscarMedidaAtual();
    }
    @Tool(description = "Buscar o nível de atividade física do usuário")
    public AtividadeFisica buscarAtividadeFisica(){
        return usuarioService.usuarioAtual().getAtividadeFisica();
    }


}

