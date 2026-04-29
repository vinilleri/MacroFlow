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
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsumoComidaService {

    private final ConsumoComidaRepository consumoComidaRepository;
    private final ConsumoRepository consumoRepository;
    private final AuthService authService;
    private final ComidaRepository comidaRepository;

    public ConsumoComidaService(ConsumoComidaRepository consumoComidaRepository, ConsumoRepository consumoRepository, AuthService authService, ComidaRepository comidaRepository) {
        this.consumoComidaRepository = consumoComidaRepository;
        this.consumoRepository = consumoRepository;
        this.authService = authService;
        this.comidaRepository = comidaRepository;

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

        BigDecimal base = comida.getValor().divide(comida.getUnidade().getBase(),2, RoundingMode.HALF_UP);
        BigDecimal quantidadeFinal = dto.getQuantidade().multiply(base);
        consumoComida.setQuantidade(quantidadeFinal);

        consumoComidaRepository.save(consumoComida);

        return new ConsumoComidaResponseDTO(consumo.getId(), comida.getId(),consumoComida.getQuantidade());
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

            BigDecimal base = comida.getValor().divide(comida.getUnidade().getBase(),2, RoundingMode.HALF_UP);
            BigDecimal quantidadeFinal = dto.getQuantidade().multiply(base);
            atualizado.setQuantidade(quantidadeFinal);

            consumoComidaRepository.save(atualizado);
            return new ConsumoComidaResponseDTO(atualizado.getConsumo().getId(),
                    atualizado.getComida().getId(), atualizado.getQuantidade());
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
