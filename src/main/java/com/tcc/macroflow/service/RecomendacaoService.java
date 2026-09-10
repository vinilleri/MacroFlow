package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.AlimentoRecomendadoDTO;
import com.tcc.macroflow.dto.MacroDTO;
import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.enums.TipoComidaReceita;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.*;
import org.springframework.stereotype.Service;

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
    private final AuthService authService;

    private final BigDecimal multiplicadorCalorias = BigDecimal.valueOf(0.30);
    private final BigDecimal multiplicadorProteina = BigDecimal.valueOf(0.40);
    private final BigDecimal multiplicadorCarboidrato= BigDecimal.valueOf(0.20);
    private final BigDecimal multiplicadorGordura = BigDecimal.valueOf(0.10);

    public RecomendacaoService(ConsumoService consumoService, MetaRepository metaRepository, ComidaUsuarioRepository comidaUsuarioRepository,
                               ComidaRepository comidaRepository, ReceitaRepository receitaRepository,  AuthService authService) {
        this.consumoService = consumoService;
        this.metaRepository = metaRepository;
        this.comidaUsuarioRepository = comidaUsuarioRepository;
        this.comidaRepository = comidaRepository;
        this.receitaRepository = receitaRepository;
        this.authService = authService;
    }
    private AlimentoRecomendadoDTO converterComidaEmAlimentoDTO(Comida comida){
        return new AlimentoRecomendadoDTO(comida.getNome(), comida.getId(),comida.getCalorias(),
                TipoComidaReceita.COMIDA,comida.getProteinas(),comida.getCarboidrato(),comida.getGordura(),BigDecimal.ZERO);
    }

    private AlimentoRecomendadoDTO converterComidaUsuarioEmAlimentoDTO(ComidaUsuario comida){
        return new AlimentoRecomendadoDTO(comida.getNome(), comida.getId(),comida.getCalorias(),
                TipoComidaReceita.COMIDA_USUARIO,comida.getProteinas(),comida.getCarboidrato(),
                comida.getGordura(),BigDecimal.ZERO);
    }
    private AlimentoRecomendadoDTO converterReceitaEmAlimentoDTO(Receita receita){

        MacroDTO macroDTO = consumoService.calcularReceita(receita.getId(), BigDecimal.ONE);

        return new AlimentoRecomendadoDTO(receita.getNome(), receita.getId(), macroDTO.getCalorias(),
                TipoComidaReceita.RECEITA, macroDTO.getProteinas(), macroDTO.getCarboidrato(),
                macroDTO.getGordura(),BigDecimal.ZERO);

    }
    private void sistemaPontuacao(AlimentoRecomendadoDTO alimentoRecomendadoDTO,
                                  BigDecimal lacunaCaloria, BigDecimal lacunaProteina
                                                    , BigDecimal lacunaCarboidrato, BigDecimal lacunaGordura, Meta meta){
        BigDecimal percentualProteina;
        BigDecimal percentualCaloria;
        BigDecimal percentualGordura;
        BigDecimal percentualCarboidrato;
        BigDecimal scoreProteina;
        BigDecimal scoreCaloria;
        BigDecimal scoreCarboidrato;
        BigDecimal scoreGordura;
        BigDecimal scoreFinal;
        if (lacunaCaloria.compareTo(BigDecimal.ZERO) > 0) {
            percentualCaloria = alimentoRecomendadoDTO.getCalorias().
                    divide(lacunaCaloria, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

            scoreCaloria = BigDecimal.valueOf(100)
                    .subtract(percentualCaloria.subtract(BigDecimal.valueOf(100)).abs());
            if(scoreCaloria.compareTo(BigDecimal.ZERO) < 0){
                scoreCaloria = BigDecimal.ZERO;
            }
        }

        else {
            scoreCaloria = BigDecimal.valueOf(100).subtract(alimentoRecomendadoDTO.getCalorias().
                    divide(meta.getCalorias(),2,RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
            if(scoreCaloria.compareTo(BigDecimal.ZERO) < 0){
                scoreCaloria = BigDecimal.ZERO;
            }
        }

        if(lacunaProteina.compareTo(BigDecimal.ZERO) >0) {
            percentualProteina = alimentoRecomendadoDTO.getProteinas().
                    divide(lacunaProteina, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            scoreProteina = BigDecimal.valueOf(100)
                    .subtract(percentualProteina.subtract(BigDecimal.valueOf(100)).abs());
            if(scoreProteina.compareTo(BigDecimal.ZERO) < 0){
                scoreProteina = BigDecimal.ZERO;
            }
        }
        else {
            scoreProteina = BigDecimal.valueOf(100).subtract(alimentoRecomendadoDTO.getProteinas().
                    divide(meta.getProteinas(),2,RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
            if(scoreProteina.compareTo(BigDecimal.ZERO) < 0){
                scoreProteina = BigDecimal.ZERO;
            }
        }
        if(lacunaCarboidrato.compareTo(BigDecimal.ZERO) >0) {
            percentualCarboidrato = alimentoRecomendadoDTO.getCarboidrato().
                    divide(lacunaCarboidrato, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            scoreCarboidrato = BigDecimal.valueOf(100)
                    .subtract(percentualCarboidrato.subtract(BigDecimal.valueOf(100)).abs());
            if(scoreCarboidrato.compareTo(BigDecimal.ZERO) < 0){
                scoreCarboidrato = BigDecimal.ZERO;
            }
        }
        else {
            scoreCarboidrato = BigDecimal.valueOf(100).subtract(alimentoRecomendadoDTO.getCarboidrato().
                    divide(meta.getCarboidrato(),2,RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
            if(scoreCarboidrato.compareTo(BigDecimal.ZERO) < 0){
                scoreCarboidrato = BigDecimal.ZERO;
            }
        }
        if(lacunaGordura.compareTo(BigDecimal.ZERO) >0) {
            percentualGordura = alimentoRecomendadoDTO.getGordura().
                    divide(lacunaGordura, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            scoreGordura = BigDecimal.valueOf(100)
                    .subtract(percentualGordura.subtract(BigDecimal.valueOf(100)).abs());


            if(scoreGordura.compareTo(BigDecimal.ZERO) < 0){
                scoreGordura = BigDecimal.ZERO;
            }
        }
        else {
            scoreGordura = BigDecimal.valueOf(100).subtract(alimentoRecomendadoDTO.getGordura().
                    divide(meta.getGordura(),2,RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
            if(scoreGordura.compareTo(BigDecimal.ZERO) < 0){
                scoreGordura = BigDecimal.ZERO;
            }
        }


        scoreFinal = scoreCaloria.multiply(multiplicadorCalorias)
                .add(scoreProteina.multiply(multiplicadorProteina))
                .add(scoreCarboidrato.multiply(multiplicadorCarboidrato))
                .add(scoreGordura.multiply(multiplicadorGordura));

        alimentoRecomendadoDTO.setScore(scoreFinal);
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
        if(!comidaUsuarios.isEmpty()) {
            comidaUsuarios.forEach(comidaUsuario -> alimentosRecomendados.add(converterComidaUsuarioEmAlimentoDTO(comidaUsuario)));
        }
       if(!receitas.isEmpty()) {
            receitas.forEach(receita -> alimentosRecomendados.add(converterReceitaEmAlimentoDTO(receita)));
        }

        for(AlimentoRecomendadoDTO alimentoRecomendadoDTO: alimentosRecomendados){
            sistemaPontuacao(alimentoRecomendadoDTO, lacunaCaloria, lacunaProteina,
                    lacunaCarboidrato, lacunaGordura, meta);
        }
        alimentosRecomendados.sort(
                (a,b) -> b.getScore().compareTo(a.getScore())
        );

        return new ArrayList<>(
                alimentosRecomendados.subList(
                        0,
                        Math.min(5, alimentosRecomendados.size())
                )
        );
    }





}
