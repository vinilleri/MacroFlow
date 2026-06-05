package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.ConsumoReceitaDTO;
import com.tcc.macroflow.dto.ConsumoReceitaResponseDTO;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumoReceitaService {

    private final ConsumoReceitaRepository consumoReceitaRepository;
    private final ConsumoRepository consumoRepository;
    private final AuthService authService;
    private final ReceitaRepository receitaRepository;

    public ConsumoReceitaService( ConsumoRepository consumoRepository,
                                 AuthService authService, ConsumoReceitaRepository consumoReceitaRepository
                               , ReceitaRepository receitaRepository) {
        this.consumoReceitaRepository = consumoReceitaRepository;
        this.consumoRepository = consumoRepository;
        this.authService = authService;
        this.receitaRepository = receitaRepository;
    }

    @Transactional
    public ConsumoReceitaResponseDTO consumirReceita(ConsumoReceitaDTO dto){
        if(dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("quantidade invalida");
        }
        Usuario usuario = authService.getUsuario();
        Consumo consumo = new Consumo();
        consumo.setDataHora(LocalDateTime.now());
        consumo.setUsuario(usuario);

        consumoRepository.save(consumo);

        ConsumoReceita consumoReceita = new ConsumoReceita();
        Receita receita = receitaRepository.findById(dto.getReceitaId()).orElseThrow(
                () -> new RuntimeException("Receita não encontrada")
        );

        if(!receita.getUsuario().getId().equals(usuario.getId())){
           throw  new RuntimeException("Receita não pertence a esse usuário");
        }
        consumoReceita.setReceita(receita);
        consumoReceita.setConsumo(consumo);
        consumoReceita.setQuantidade(dto.getQuantidade());
        consumoReceitaRepository.save(consumoReceita);

        return converterEmDTO(consumoReceita);
    }


    private ConsumoReceitaResponseDTO converterEmDTO(ConsumoReceita consumoReceita){
        return new ConsumoReceitaResponseDTO(consumoReceita.getId(),
                consumoReceita.getConsumo().getId(),
                 consumoReceita.getReceita().getId(),
                consumoReceita.getQuantidade());
    }

    public List<ConsumoReceitaResponseDTO> listarConsumoReceita(){
        Usuario usuario = authService.getUsuario();


        List<ConsumoReceita> consumoReceitas = consumoReceitaRepository.findAllByConsumoUsuarioId(usuario.getId());

        return consumoReceitas.stream()
                .map(this::converterEmDTO)
                .toList();
    }

    public ConsumoReceitaResponseDTO getConsumoReceita(Long id){
        Usuario usuario = authService.getUsuario();

        ConsumoReceita consumoReceita = consumoReceitaRepository.findByIdAndConsumoUsuarioId(id, usuario.getId()).orElseThrow(
                () -> new RuntimeException("Nenhum consumo encontrado com esse id")
        );

        return converterEmDTO(consumoReceita);
    }


    @Transactional
    public ConsumoReceitaResponseDTO atualizarConsumirReceita(ConsumoReceitaDTO dto, Long id){
        if(dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("quantidade invalida");
        }
        ConsumoReceita atualizado = consumoReceitaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Consumo não encontrado")
        );
        Usuario usuario = authService.getUsuario();
        if(atualizado.getConsumo().getUsuario().getId().equals(usuario.getId())) {
            Receita receita = receitaRepository.findById(dto.getReceitaId()).orElseThrow(
                    () -> new RuntimeException("Receita não encontrada")
            );
            if(!receita.getUsuario().getId().equals(usuario.getId())){
                throw  new RuntimeException("Receita não pertence a esse usuário");
            }
            atualizado.setReceita(receita);
            atualizado.setQuantidade(dto.getQuantidade());
            consumoReceitaRepository.save(atualizado);
             return converterEmDTO(atualizado);
        }
        else throw  new RuntimeException("Consumo não pertence a esse usuário");
    }

    @Transactional
    public void deletarConsumoReceita(Long consumoReceitaId) {
        Usuario usuario = authService.getUsuario();
        ConsumoReceita consumoReceita = consumoReceitaRepository.findById(consumoReceitaId).orElseThrow(
                () -> new RuntimeException("Consumo receita não encontrado")
        );
        Consumo consumo = consumoReceita.getConsumo();
        if(consumoReceita.getConsumo().getUsuario().getId().equals(usuario.getId())) {
            int quantidade = consumoReceitaRepository.countByConsumoId(consumo.getId());

            consumoReceitaRepository.delete(consumoReceita);

            if (quantidade <= 1) {
                consumoRepository.delete(consumo);
            }
        }
        else throw new RuntimeException("Consumo não pertence a esse usuário");
    }

}
