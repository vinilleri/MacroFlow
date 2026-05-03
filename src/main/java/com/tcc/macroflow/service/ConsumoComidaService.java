package com.tcc.macroflow.service;


import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.dto.ConsumoComidaDTO;
import com.tcc.macroflow.dto.ConsumoComidaResponseDTO;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class ConsumoComidaService {

    private final ConsumoComidaRepository consumoComidaRepository;
    private final ConsumoComidaUsuarioRepository consumoUsuarioRepository;
    private final ConsumoRepository consumoRepository;
    private final AuthService authService;
    private final ComidaRepository comidaRepository;
    private final ComidaUsuarioRepository comidaUsuarioRepository;
    public ConsumoComidaService(ConsumoComidaRepository consumoComidaRepository, ConsumoComidaUsuarioRepository consumoUsuarioRepository, ConsumoRepository consumoRepository, AuthService authService, ComidaRepository comidaRepository, ComidaUsuarioRepository comidaUsuarioRepository) {
        this.consumoComidaRepository = consumoComidaRepository;
        this.consumoUsuarioRepository = consumoUsuarioRepository;
        this.consumoRepository = consumoRepository;
        this.authService = authService;
        this.comidaRepository = comidaRepository;
        this.comidaUsuarioRepository = comidaUsuarioRepository;
    }

    private BigDecimal calcularQuantidade(Comida comida, ConsumoComidaDTO dto){
        BigDecimal base = comida.getValor().divide(comida.getUnidade().getBase(), 2, RoundingMode.HALF_UP);
        return dto.getQuantidade().multiply(base);
    }

    private BigDecimal calcularQuantidadeUsuario(ComidaUsuario comida, ConsumoComidaDTO dto){
        BigDecimal base = comida.getValor().divide(comida.getUnidade().getBase(), 2, RoundingMode.HALF_UP);
        return dto.getQuantidade().multiply(base);
    }

    private ConsumoComida consumirComidaSistema(ConsumoComidaDTO dto, Consumo consumo){
        ConsumoComida consumoComida = new ConsumoComida();
        Comida comida = comidaRepository.findById(dto.getComidaId()).orElseThrow(
                () -> new RuntimeException("Comida não encontrada")
        );

        consumoComida.setComida(comida);
        consumoComida.setConsumo(consumo);

        BigDecimal quantidadeFinal = calcularQuantidade(comida,dto);
        consumoComida.setQuantidade(quantidadeFinal);

        return consumoComida;
    }
    private ConsumoComidaUsuario consumirComidaUsuario(ConsumoComidaDTO dto, Consumo consumo){
        ConsumoComidaUsuario consumoComidaUsuario = new ConsumoComidaUsuario();
        ComidaUsuario comida = comidaUsuarioRepository.findById(dto.getComidaId()).orElseThrow(
                () -> new RuntimeException("Comida não encontrada")
        );
        consumoComidaUsuario.setComidaUsuario(comida);
        consumoComidaUsuario.setConsumo(consumo);

        BigDecimal quantidadeFinal = calcularQuantidadeUsuario(comida,dto);
        consumoComidaUsuario.setQuantidade(quantidadeFinal);

        return consumoComidaUsuario;
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

        if(dto.getOrigem().equals(Origem.SISTEMA)) {
            ConsumoComida consumoComida = consumirComidaSistema(dto,consumo);
            consumoComidaRepository.save(consumoComida);
            return new ConsumoComidaResponseDTO(consumo.getId(), consumoComida.getComida().getId(), consumoComida.getQuantidade());
        }
        else{
            ConsumoComidaUsuario consumoComidaUsuario = consumirComidaUsuario(dto,consumo);
            consumoUsuarioRepository.save(consumoComidaUsuario);
            return new ConsumoComidaResponseDTO(consumo.getId(), consumoComidaUsuario.getComidaUsuario().getId(),
                    consumoComidaUsuario.getQuantidade());
        }
    }

    private ConsumoComida atualizarConsumoSistema(Usuario usuario, ConsumoComidaDTO dto,Long id){
        ConsumoComida atualizado = consumoComidaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Consumo não encontrado")
        );
        if (atualizado.getConsumo().getUsuario().getId().equals(usuario.getId())) {
            Comida comida = comidaRepository.findById(dto.getComidaId()).orElseThrow(
                    () -> new RuntimeException("Comida não encontrada")
            );
            atualizado.setComida(comida);
            BigDecimal quantidadeFinal = calcularQuantidade(comida,dto);
            atualizado.setQuantidade(quantidadeFinal);

            return atualizado;
        } else throw new RuntimeException("Consumo não pertence a esse usuário");
    }
    private ConsumoComidaUsuario atualizarConsumoUsuario(Usuario usuario, ConsumoComidaDTO dto,Long id){
        ConsumoComidaUsuario atualizado = consumoUsuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Consumo não encontrado")
        );
        if (atualizado.getConsumo().getUsuario().getId().equals(usuario.getId())) {
            ComidaUsuario comida = comidaUsuarioRepository.findById(dto.getComidaId()).orElseThrow(
                    () -> new RuntimeException("Comida não encontrada")
            );
            atualizado.setComidaUsuario(comida);
            BigDecimal quantidadeFinal = calcularQuantidadeUsuario(comida,dto);
            atualizado.setQuantidade(quantidadeFinal);

            return atualizado;
        } else throw new RuntimeException("Consumo não pertence a esse usuário");
    }

    @Transactional
    public ConsumoComidaResponseDTO atualizarConsumoComida(ConsumoComidaDTO dto, Long id) {
        if (dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("quantidade invalida");
        }
        Usuario usuario = authService.getUsuario();
        if (dto.getOrigem().equals(Origem.SISTEMA)) {
                 ConsumoComida atualizado = atualizarConsumoSistema(usuario,dto,id);
                consumoComidaRepository.save(atualizado);
                return new ConsumoComidaResponseDTO(atualizado.getConsumo().getId(),
                        atualizado.getComida().getId(), atualizado.getQuantidade());

        } else {
                 ConsumoComidaUsuario atualizado = atualizarConsumoUsuario(usuario,dto,id);
                consumoUsuarioRepository.save(atualizado);
                return new ConsumoComidaResponseDTO(atualizado.getConsumo().getId(),
                        atualizado.getComidaUsuario().getId(), atualizado.getQuantidade());

        }
    }

    private void  deletarConsumoSistema(Long id, Usuario usuario){
        ConsumoComida consumoComida = consumoComidaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Consumo comida não encontrado")
        );
        Consumo consumo = consumoComida.getConsumo();
        if (consumoComida.getConsumo().getUsuario().getId().equals(usuario.getId())) {
            int quantidade = consumoComidaRepository.countByConsumoId(consumo.getId());

            consumoComidaRepository.delete(consumoComida);

            if (quantidade <= 1) {
                consumoRepository.delete(consumo);
            }
        } else throw new RuntimeException("Consumo não pertence a esse usuário");
    }

    private void deletarConsumoUsuario(Long id, Usuario usuario){
        ConsumoComidaUsuario consumoComida = consumoUsuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Consumo comida não encontrado")
        );
        Consumo consumo = consumoComida.getConsumo();
        if (consumoComida.getConsumo().getUsuario().getId().equals(usuario.getId())) {
            int quantidade = consumoUsuarioRepository.countByConsumoId(consumo.getId());

            consumoUsuarioRepository.delete(consumoComida);

            if (quantidade <= 1) {
                consumoRepository.delete(consumo);
            }
        } else throw new RuntimeException("Consumo não pertence a esse usuário");
    }

    @Transactional
    public void deletarConsumoComida(Origem origem, Long consumoComidaId) {
        Usuario usuario = authService.getUsuario();

        if (origem.equals(Origem.SISTEMA)) {

            deletarConsumoSistema(consumoComidaId, usuario);
        } else {
            deletarConsumoUsuario(consumoComidaId, usuario);
        }
    }
}