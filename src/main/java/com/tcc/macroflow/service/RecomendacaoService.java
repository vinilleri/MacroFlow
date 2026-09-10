package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.AlimentoRecomendadoDTO;
import com.tcc.macroflow.dto.MacroDTO;
import com.tcc.macroflow.enums.TipoComidaReceita;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.*;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecomendacaoService {

    private final ConsumoService consumoService;
    private final MetaRepository metaRepository;
    private final ComidaUsuarioRepository comidaUsuarioRepository;
    private final ComidaRepository comidaRepository;
    private final ReceitaRepository receitaRepository;
    private final ReceitaItemRepository receitaItemRepository;
    private final AuthService authService;

    public RecomendacaoService(ConsumoService consumoService, MetaRepository metaRepository, ComidaUsuarioRepository comidaUsuarioRepository, ComidaRepository comidaRepository, ReceitaRepository receitaRepository, ReceitaItemRepository receitaItemRepository, AuthService authService) {
        this.consumoService = consumoService;
        this.metaRepository = metaRepository;
        this.comidaUsuarioRepository = comidaUsuarioRepository;
        this.comidaRepository = comidaRepository;
        this.receitaRepository = receitaRepository;
        this.receitaItemRepository = receitaItemRepository;
        this.authService = authService;
    }
    private AlimentoRecomendadoDTO converterComidaEmAlimentoDTO(Comida comida){
        return new AlimentoRecomendadoDTO(comida.getNome(), comida.getId(),comida.getCalorias(),
                TipoComidaReceita.COMIDA,comida.getProteinas(),comida.getCarboidrato(),comida.getGordura());
    }

    private AlimentoRecomendadoDTO converterComidaUsuarioEmAlimentoDTO(ComidaUsuario comida){
        return new AlimentoRecomendadoDTO(comida.getNome(), comida.getId(),comida.getCalorias(),
                TipoComidaReceita.COMIDA,comida.getProteinas(),comida.getCarboidrato(),comida.getGordura());
    }
    private AlimentoRecomendadoDTO converterReceitaEmAlimentoDTO(Receita receita){

        MacroDTO macroDTO = consumoService.calcularReceita(receita.getId(), BigDecimal.ONE);

        return new AlimentoRecomendadoDTO(receita.getNome(), receita.getId(), macroDTO.getCalorias(), TipoComidaReceita.RECEITA, macroDTO.getProteinas(), macroDTO.getCarboidrato(), macroDTO.getGordura());

    }


    public ArrayList<AlimentoRecomendadoDTO> alimentosRecomendados(){
        Usuario usuario = authService.getUsuario();

        MacroDTO consumo = consumoService.somarConsumoDia();
        Meta meta = metaRepository.findByUsuarioIdAndAtiva(usuario.getId(), true).orElseThrow(
                () -> new RuntimeException("Meta do usuário não encontrada")
        );

        BigDecimal lacunaCaloria = meta.getCalorias().subtract(consumo.getCalorias());
        BigDecimal lacunaProteina = meta.getProteinas().subtract(consumo.getProteinas());
        BigDecimal lacunaCarboidrato = meta.getCarboidrato().subtract(consumo.getCarboidrato());
        BigDecimal lacunaGordura = meta.getGordura().subtract(consumo.getGordura());

        List<Comida> comidas = comidaRepository.findAll();
        List<ComidaUsuario> comidaUsuarios = comidaUsuarioRepository.findAllByUsuarioId(usuario.getId());
        List<Receita> receitas = receitaRepository.findAllByUsuarioId(usuario.getId());
        ArrayList<AlimentoRecomendadoDTO> alimentosRecomendados = new ArrayList<>();

       comidas.forEach(comida -> alimentosRecomendados.add(converterComidaEmAlimentoDTO(comida)));
       comidaUsuarios.forEach(comidaUsuario -> alimentosRecomendados.add(converterComidaUsuarioEmAlimentoDTO(comidaUsuario)));
       receitas.forEach(receita -> alimentosRecomendados.add(converterReceitaEmAlimentoDTO(receita)));

       for(AlimentoRecomendadoDTO alimentoRecomendadoDTO:alimentosRecomendados){

           if (lacunaCaloria.compareTo(BigDecimal.ZERO) > 0) {
               BigDecimal percentualCaloria = alimentoRecomendadoDTO.getCalorias().
                       divide(lacunaCaloria, 2, RoundingMode.UP).multiply(BigDecimal.valueOf(100));
           }

           BigDecimal percentualProteina = alimentoRecomendadoDTO.getProteinas().
                   divide(lacunaProteina,2, RoundingMode.UP).multiply(BigDecimal.valueOf(100));

           BigDecimal percentualCarboidrato = alimentoRecomendadoDTO.getCarboidrato().
                   divide(lacunaCarboidrato,2, RoundingMode.UP).multiply(BigDecimal.valueOf(100));


          BigDecimal percentualGordura = alimentoRecomendadoDTO.getGordura().
                   divide(lacunaGordura,2, RoundingMode.UP).multiply(BigDecimal.valueOf(100));
       }





    }





}
