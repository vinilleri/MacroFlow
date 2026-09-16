package com.tcc.macroflow.ia.tools;

import com.tcc.macroflow.dto.AlimentoRecomendadoDTO;
import com.tcc.macroflow.dto.ComidaDTO;
import com.tcc.macroflow.dto.ComidaResponseDTO;
import com.tcc.macroflow.helper.BuscarAlimentoNome;
import com.tcc.macroflow.service.ComidaService;
import com.tcc.macroflow.service.RecomendacaoService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ComidaTools {
    private final ComidaService comidaService;
    private final RecomendacaoService recomendacaoService;
    private final BuscarAlimentoNome buscarAlimentoNome;
    public ComidaTools(ComidaService comidaService, RecomendacaoService recomendacaoService, BuscarAlimentoNome buscarAlimentoNome) {
        this.comidaService = comidaService;
        this.recomendacaoService = recomendacaoService;
        this.buscarAlimentoNome = buscarAlimentoNome;
    }
    @Tool(description = "Busca qualquer alimento pelo nome, seja receita, comida ou comida usuário")
    public AlimentoRecomendadoDTO buscarAlimentoNome(String nome) {

        return buscarAlimentoNome.buscarComidaNome(nome);
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
