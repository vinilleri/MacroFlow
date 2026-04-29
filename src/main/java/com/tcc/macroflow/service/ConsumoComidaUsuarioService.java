package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.ConsumoComidaDTO;
import com.tcc.macroflow.dto.ConsumoComidaResponseDTO;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ConsumoComidaUsuarioService {

        private final ConsumoComidaUsuarioRepository consumoComidaUsuarioRepository;
        private final ConsumoRepository consumoRepository;
        private final AuthService authService;
        private final ComidaUsuarioRepository comidaUsuarioRepository;
        private final UnidadeRepository unidadeRepository;
        public ConsumoComidaUsuarioService(ConsumoComidaUsuarioRepository consumoComidaUsuarioRepositoryRepository,
                                           ConsumoRepository consumoRepository, AuthService authService,
                                           ComidaUsuarioRepository comidaUsuarioRepository, UnidadeRepository unidadeRepository) {
            this.consumoComidaUsuarioRepository = consumoComidaUsuarioRepositoryRepository;
            this.consumoRepository = consumoRepository;
            this.authService = authService;
            this.comidaUsuarioRepository = comidaUsuarioRepository;
            this.unidadeRepository = unidadeRepository;
        }

        @Transactional
        public ConsumoComidaResponseDTO consumirComida(ConsumoComidaDTO dto){
            if(dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0){
                throw new RuntimeException("quantidade invalida");
            }
            Usuario usuario = authService.getUsuario();
            Consumo consumo = new Consumo();
            consumo.setDataHora(LocalDateTime.now());
            consumo.setUsuario(usuario);

            consumoRepository.save(consumo);

            ConsumoComidaUsuario consumoComidaUsuario = new ConsumoComidaUsuario();
            ComidaUsuario comidaUsuario = comidaUsuarioRepository.findById(dto.getComidaId()).orElseThrow(
                    () -> new RuntimeException("Comida não encontrada")
            );
            if(!comidaUsuario.getUsuario().getId().equals(usuario.getId())){
                throw  new RuntimeException("Comida não pertence a esse usuário");
            }
            consumoComidaUsuario.setComidaUsuario(comidaUsuario);
            consumoComidaUsuario.setConsumo(consumo);
            Unidade unidade = unidadeRepository.findById(dto.getUnidadeId()).orElseThrow(
                    () -> new RuntimeException("Unidade não encontrada")
            );
            consumoComidaUsuario.setUnidade(unidade);
            consumoComidaUsuario.setQuantidade(dto.getQuantidade());

            consumoComidaUsuarioRepository.save(consumoComidaUsuario);

            return new ConsumoComidaResponseDTO(consumo.getId(), comidaUsuario.getId(),consumoComidaUsuario.getQuantidade(), unidade.getId());
        }


        @Transactional
        public ConsumoComidaResponseDTO atualizarConsumoComida(ConsumoComidaDTO dto, Long id){
            if(dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0){
                throw new RuntimeException("quantidade invalida");
            }
            ConsumoComidaUsuario atualizado = consumoComidaUsuarioRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("Consumo não encontrado")
            );
            Usuario usuario = authService.getUsuario();
            if(atualizado.getConsumo().getUsuario().getId().equals(usuario.getId())) {
                ComidaUsuario comidaUsuario = comidaUsuarioRepository.findById(dto.getComidaId()).orElseThrow(
                        () -> new RuntimeException("Comida não encontrada")
                );
                if(!comidaUsuario.getUsuario().getId().equals(usuario.getId())){
                    throw  new RuntimeException("Comida não pertence a esse usuário");
                }
                atualizado.setComidaUsuario(comidaUsuario);
                atualizado.setQuantidade(dto.getQuantidade());
                Unidade unidade = unidadeRepository.findById(dto.getUnidadeId()).orElseThrow(
                        () -> new RuntimeException("Unidade não encontrada")
                );
                atualizado.setUnidade(unidade);
                consumoComidaUsuarioRepository.save(atualizado);
                return new ConsumoComidaResponseDTO(atualizado.getConsumo().getId(),
                        atualizado.getComidaUsuario().getId(), atualizado.getQuantidade(), atualizado.getUnidade().getId());
            }
            else throw  new RuntimeException("Consumo não pertence a esse usuário");
        }

        @Transactional
        public void deletarConsumoComida(Long consumoComidaId) {
            Usuario usuario = authService.getUsuario();
            ConsumoComidaUsuario consumoComidaUsuario = consumoComidaUsuarioRepository.findById(consumoComidaId).orElseThrow(
                    () -> new RuntimeException("Consumo comida não encontrado")
            );
            Consumo consumo = consumoComidaUsuario.getConsumo();
            if(consumoComidaUsuario.getConsumo().getUsuario().getId().equals(usuario.getId())) {
                int quantidade = consumoComidaUsuarioRepository.countByConsumoId(consumo.getId());

                consumoComidaUsuarioRepository.delete(consumoComidaUsuario);

                if (quantidade <= 1) {
                    consumoRepository.delete(consumo);
                }
            }
            else throw new RuntimeException("Consumo não pertence a esse usuário");
        }

    }
