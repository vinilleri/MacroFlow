package com.tcc.macroflow.ia.tools;

import com.tcc.macroflow.dto.AlimentoRecomendadoDTO;
import com.tcc.macroflow.dto.ConsumoItemDTO;
import com.tcc.macroflow.dto.MacroDTO;
import com.tcc.macroflow.enums.TipoComidaReceita;
import com.tcc.macroflow.enums.TipoConsumo;
import com.tcc.macroflow.model.Consumo;
import com.tcc.macroflow.service.ConsumoService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ConsumoTools {

    private final ConsumoService consumoService;

    public ConsumoTools(ConsumoService consumoService) {
        this.consumoService = consumoService;
    }

    public ConsumoItemDTO converterEmConsumoItemDTO(AlimentoRecomendadoDTO itemDTO, BigDecimal quantidade, BigDecimal valor){
        TipoConsumo tipo = (itemDTO.getTipo() == TipoComidaReceita.COMIDA) ? TipoConsumo.COMIDA :
                (itemDTO.getTipo() == TipoComidaReceita.COMIDA_USUARIO) ? TipoConsumo.COMIDA_USUARIO :
                TipoConsumo.RECEITA;


        return  new ConsumoItemDTO(itemDTO.getId(),tipo,itemDTO.getNome(),quantidade,valor);
    }


    @Tool(description = "Consulta o consumo nutricional do usuário no dia atual.")
      public MacroDTO buscarConsumoDia(){
        return  consumoService.somarConsumoDia();
    }
    @Tool(description = "Cria um novo registro de consumo para o usuário, lembre-se de pesquisar o alimento pelo nome antes de registrar")
    public Consumo registrarConsumo(AlimentoRecomendadoDTO itemDTO, BigDecimal quantidade, BigDecimal valor){

       ConsumoItemDTO consumo = converterEmConsumoItemDTO(itemDTO,quantidade,valor);
        return consumoService.consumirItem(consumo);
    }
}
