package com.tcc.macroflow.service;


import com.tcc.macroflow.dto.ComidaResponseDTO;
import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.dto.ConsumoComidaDTO;
import com.tcc.macroflow.dto.ConsumoComidaResponseDTO;
import com.tcc.macroflow.helper.CalcularQuantidade;
import com.tcc.macroflow.model.*;
import com.tcc.macroflow.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    private ConsumoComida consumirComidaSistema(ConsumoComidaDTO dto, Consumo consumo){
        ConsumoComida consumoComida = new ConsumoComida();
        Comida comida = comidaRepository.findById(dto.getComidaId()).orElseThrow(
                () -> new RuntimeException("Comida não encontrada")
        );

        consumoComida.setComida(comida);
        consumoComida.setConsumo(consumo);

        BigDecimal quantidadeFinal =CalcularQuantidade.calcularQuantidade(dto.getQuantidade(),dto.getValor()
                ,CalcularQuantidade.converterComidaEmDto(comida));
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

        BigDecimal quantidadeFinal = CalcularQuantidade.calcularQuantidade(dto.getQuantidade(),dto.getValor()
                ,CalcularQuantidade.converterComidaUsuarioEmDto(comida));
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
            return converterEmDTOSistema(consumoComida);
        }
        else{
            ConsumoComidaUsuario consumoComidaUsuario = consumirComidaUsuario(dto,consumo);
            consumoUsuarioRepository.save(consumoComidaUsuario);
            return converterEmDTOUsuario(consumoComidaUsuario);
        }
    }


        private ConsumoComidaResponseDTO converterEmDTOUsuario(ConsumoComidaUsuario consumoComidaUsuario){
            return new
                    ConsumoComidaResponseDTO(consumoComidaUsuario.getId(),
                    consumoComidaUsuario.getConsumo().getId(),
                    consumoComidaUsuario.getComidaUsuario().getId(),
                    consumoComidaUsuario.getQuantidade(),
                    Origem.USUARIO);
        }
    private ConsumoComidaResponseDTO converterEmDTOSistema(ConsumoComida consumoComida){
        return new
                ConsumoComidaResponseDTO(consumoComida.getId(),
                consumoComida.getConsumo().getId(),
                consumoComida.getComida().getId(),
                consumoComida.getQuantidade(),
                Origem.SISTEMA);
    }

    public List<ConsumoComidaResponseDTO> listarConsumosComida(){
        Usuario usuario = authService.getUsuario();

        List<ConsumoComida> consumoComidas = consumoComidaRepository.findAllByConsumoUsuarioId(usuario.getId());
        List<ConsumoComidaUsuario> consumoComidaUsuarios = consumoUsuarioRepository.findAllByConsumoUsuarioId(usuario.getId());

        List<ConsumoComidaResponseDTO> listaConjunta = new ArrayList<>();

            listaConjunta.addAll(consumoComidas.stream()
                    .map(this::converterEmDTOSistema)
                    .toList());
            listaConjunta.addAll(consumoComidaUsuarios.stream()
                .map(this::converterEmDTOUsuario)
                .toList());

         return listaConjunta;

    }

    public ConsumoComidaResponseDTO getComida(Long id, Origem origem){
            Usuario usuario = authService.getUsuario();

        if(origem.equals(Origem.SISTEMA)){

                ConsumoComida consumoComida = consumoComidaRepository.findByIdAndConsumoUsuarioId(id, usuario.getId())
                        .orElseThrow(
                                () -> new RuntimeException("Comida não encontrada")
                        );
              return  converterEmDTOSistema(consumoComida);
            }
        else{
            ConsumoComidaUsuario consumoComidaUsuario = consumoUsuarioRepository.findByIdAndConsumoUsuarioId(id, usuario.getId())
                    .orElseThrow(
                            () -> new RuntimeException("Comida não encontrada")
                    );
            return  converterEmDTOUsuario(consumoComidaUsuario);
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
            BigDecimal quantidadeFinal =  CalcularQuantidade.calcularQuantidade(dto.getQuantidade(),dto.getValor()
                    ,CalcularQuantidade.converterComidaEmDto(comida));
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
            BigDecimal quantidadeFinal = CalcularQuantidade.calcularQuantidade(dto.getQuantidade(),dto.getValor()
                    ,CalcularQuantidade.converterComidaUsuarioEmDto(comida));
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
                return converterEmDTOSistema(atualizado);

        } else {
                 ConsumoComidaUsuario atualizado = atualizarConsumoUsuario(usuario,dto,id);
                consumoUsuarioRepository.save(atualizado);
                return converterEmDTOUsuario(atualizado);

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