package com.tcc.macroflow.helper;

import com.tcc.macroflow.dto.AlimentoRecomendadoDTO;
import com.tcc.macroflow.dto.ComidaResponseDTO;
import com.tcc.macroflow.dto.MacroDTO;
import com.tcc.macroflow.dto.ReceitaResponseDTO;
import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.enums.TipoComidaReceita;
import com.tcc.macroflow.service.ComidaService;
import com.tcc.macroflow.service.ConsumoService;
import com.tcc.macroflow.service.ReceitaService;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;

@Component
public class BuscarAlimentoNome {
 private  final ComidaService comidaService;
private final ReceitaService receitaService;
private final ConsumoService consumoService;
    public BuscarAlimentoNome(ComidaService comidaService, ReceitaService receitaService, ConsumoService consumoService) {
        this.comidaService = comidaService;
        this.receitaService = receitaService;
        this.consumoService = consumoService;
    }
    private AlimentoRecomendadoDTO converterMacroDTOParaAlimentoRecomendadoDTO(MacroDTO macroDTO, ReceitaResponseDTO receitaResponseDTO ){
        return  new AlimentoRecomendadoDTO(receitaResponseDTO.getNome(), receitaResponseDTO.getId(),
                macroDTO.getCalorias(), TipoComidaReceita.RECEITA,macroDTO.getProteinas(),macroDTO.getCarboidrato(),
        macroDTO.getGordura(), BigDecimal.ZERO);
    }

    private AlimentoRecomendadoDTO converterComidaDTOParaAlimentoRecomendadoDTO(ComidaResponseDTO comidaResponseDTO){
         TipoComidaReceita tipo;
         tipo = (comidaResponseDTO.getOrigem() == Origem.SISTEMA) ? TipoComidaReceita.COMIDA: TipoComidaReceita.COMIDA_USUARIO;

        return  new AlimentoRecomendadoDTO(comidaResponseDTO.getNome(), comidaResponseDTO.getId(), comidaResponseDTO.getCalorias(),
               tipo,comidaResponseDTO.getProteinas(), comidaResponseDTO.getCarboidrato(), comidaResponseDTO.getGordura(), BigDecimal.ZERO);

    }



    public AlimentoRecomendadoDTO buscarComidaNome(String nome){
     ComidaResponseDTO comidaResponseDTO = comidaService.buscarComidaNome(nome);

     if(comidaResponseDTO == null){
         ReceitaResponseDTO receitaResponseDTO = receitaService.buscarReceitaNome(nome);

         if(receitaResponseDTO != null) {
          MacroDTO macroDTO= consumoService.calcularReceita
                  (receitaResponseDTO.getId(), BigDecimal.ONE);

          return converterMacroDTOParaAlimentoRecomendadoDTO(macroDTO, receitaResponseDTO);
         }
         else{
             return null;
         }
     }

     return converterComidaDTOParaAlimentoRecomendadoDTO(comidaResponseDTO);


    }
}
