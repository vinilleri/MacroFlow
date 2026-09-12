package com.tcc.macroflow.ia.tools;

import com.tcc.macroflow.dto.AlimentoRecomendadoDTO;
import com.tcc.macroflow.dto.ComidaDTO;
import com.tcc.macroflow.dto.ComidaResponseDTO;
import com.tcc.macroflow.service.ComidaService;
import com.tcc.macroflow.service.RecomendacaoService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ComidaTools {
    private final ComidaService comidaService;
    private final RecomendacaoService recomendacaoService;
    public ComidaTools(ComidaService comidaService, RecomendacaoService recomendacaoService) {
        this.comidaService = comidaService;
        this.recomendacaoService = recomendacaoService;
    }
    @Tool(description = "Busca uma comida pelo nome.")
    public ComidaResponseDTO buscarComidaNome(String nome) {
        return comidaService.buscarComidaNome(nome);
    }
    @Tool(description = "Cria uma comida no sistema.")
    public ComidaResponseDTO criarComida(ComidaDTO comidaDTO){
        return comidaService.salvar(comidaDTO);
    }

    @Tool(description = "Buscar cinco alimentos recomendados")
    public ArrayList<AlimentoRecomendadoDTO> buscarRecomendados(){
        return recomendacaoService.alimentosRecomendados();
    }
}
