package com.tcc.macroflow.service;


import com.tcc.macroflow.dto.ConsumoComidaDTO;
import com.tcc.macroflow.dto.ConsumoComidaResponseDTO;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.ComidaRepository;
import com.tcc.macroflow.repository.ConsumoComidaRepository;
import com.tcc.macroflow.repository.ConsumoRepository;
import com.tcc.macroflow.repository.UnidadeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsumoComidaService {

    private final ConsumoComidaRepository consumoComidaRepository;
    private final ConsumoRepository consumoRepository;
    private final AuthService authService;
    private final ComidaRepository comidaRepository;
    private final UnidadeRepository unidadeRepository;
    public ConsumoComidaService(ConsumoComidaRepository consumoComidaRepository, ConsumoRepository consumoRepository, AuthService authService, ComidaRepository comidaRepository, UnidadeRepository unidadeRepository) {
        this.consumoComidaRepository = consumoComidaRepository;
        this.consumoRepository = consumoRepository;
        this.authService = authService;
        this.comidaRepository = comidaRepository;
        this.unidadeRepository = unidadeRepository;
    }

    @Transactional
    public ConsumoComidaResponseDTO consumirComida(ConsumoComidaDTO dto){
        if(dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("quantidade invalida");
        }
        Consumo consumo = new Consumo();
        consumo.setDataHora(LocalDateTime.now());
        consumo.setUsuario(authService.getUsuario());

        consumoRepository.save(consumo);

        ConsumoComida consumoComida = new ConsumoComida();
        Comida comida = comidaRepository.findById(dto.getComidaId()).orElseThrow(
                () -> new RuntimeException("Comida não encontrada")
        );
        consumoComida.setComida(comida);
        consumoComida.setConsumo(consumo);
        Unidade unidade = unidadeRepository.findById(dto.getUnidadeId()).orElseThrow(
                () -> new RuntimeException("Unidade não encontrada")
        );
        consumoComida.setUnidade(unidade);
        consumoComida.setQuantidade(dto.getQuantidade());

        consumoComidaRepository.save(consumoComida);

        return new ConsumoComidaResponseDTO(consumo.getId(), comida.getId(),consumoComida.getQuantidade(), unidade.getId());
    }


    @Transactional
    public ConsumoComidaResponseDTO atualizarConsumoComida(ConsumoComidaDTO dto, Long id){
        if(dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("quantidade invalida");
        }
        ConsumoComida atualizado = consumoComidaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Consumo não encontrado")
        );
        Usuario usuario = authService.getUsuario();
        if(atualizado.getConsumo().getUsuario().getId().equals(usuario.getId())) {
            Comida comida = comidaRepository.findById(dto.getComidaId()).orElseThrow(
                    () -> new RuntimeException("Comida não encontrada")
            );
            atualizado.setComida(comida);
            atualizado.setQuantidade(dto.getQuantidade());
            Unidade unidade = unidadeRepository.findById(dto.getUnidadeId()).orElseThrow(
                    () -> new RuntimeException("Unidade não encontrada")
            );
            atualizado.setUnidade(unidade);
            consumoComidaRepository.save(atualizado);
            return new ConsumoComidaResponseDTO(atualizado.getConsumo().getId(),
                    atualizado.getComida().getId(), atualizado.getQuantidade(), atualizado.getUnidade().getId());
        }
        else throw  new RuntimeException("Consumo não pertence a esse usuário");
    }

    @Transactional
    public void deletarConsumoComida(Long consumoComidaId) {
        Usuario usuario = authService.getUsuario();
        ConsumoComida consumoComida = consumoComidaRepository.findById(consumoComidaId).orElseThrow(
                () -> new RuntimeException("Consumo comida não encontrado")
        );
        Consumo consumo = consumoComida.getConsumo();
        if(consumoComida.getConsumo().getUsuario().getId().equals(usuario.getId())) {
           int quantidade = consumoComidaRepository.countByConsumoId(consumo.getId());

                consumoComidaRepository.delete(consumoComida);

            if (quantidade <= 1) {
                consumoRepository.delete(consumo);
            }
        }
        else throw new RuntimeException("Consumo não pertence a esse usuário");
    }

}
