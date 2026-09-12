package com.tcc.macroflow.ia.tools;

import com.tcc.macroflow.dto.MetaDTO;
import com.tcc.macroflow.dto.MetaRequisicaoDTO;
import com.tcc.macroflow.dto.MetaResponseDTO;
import com.tcc.macroflow.dto.ObjetivoResponseDTO;
import com.tcc.macroflow.enums.TipoMeta;
import com.tcc.macroflow.service.MetaService;
import com.tcc.macroflow.service.ObjetivoService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class MetaTools {
    private final MetaService metaService;
    private final ObjetivoService objetivoService;
    public MetaTools(MetaService metaService,  ObjetivoService objetivoService) {
        this.metaService = metaService;
        this.objetivoService = objetivoService;
    }
    @Tool(description = "  Cria uma meta nutricional manual personalizada para o objetivo atual do usuário.\n" +
            "        Não é necessário informar o ID do objetivo, pois ele é obtido automaticamente.\n" +
            "        Os valores informados serão validados pelo sistema antes da criação.")
    public MetaResponseDTO criarMeta(MetaRequisicaoDTO dto){
        ObjetivoResponseDTO objetivo = objetivoService.objetivoAtual();
        MetaDTO metaDTO = new MetaDTO(objetivo.getId(), TipoMeta.MANUAL,dto.getCalorias(),
                dto.getProteinas(),dto.getCarboidrato(),dto.getGordura());
        return metaService.criarMeta(metaDTO);
    }

    @Tool(description = "Consulta a meta nutricional atual do usuário.")
    public MetaResponseDTO buscarMetaAtual(){
        return metaService.buscarMetaAtual();
    }
}
