package com.tcc.macroflow.service;


import com.tcc.macroflow.dto.ComidaDTO;
import com.tcc.macroflow.dto.ComidaResponseDTO;
import com.tcc.macroflow.model.Comida;
import com.tcc.macroflow.model.ComidaUsuario;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.ComidaRepository;
import com.tcc.macroflow.repository.ComidaUsuarioRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ComidaService {

private final ComidaUsuarioRepository comidaUsuarioRepository;
private final ComidaRepository comidaRepository;
private final AuthService authService;
    public ComidaService(ComidaUsuarioRepository comidaRepository, ComidaRepository comidaRepository1, AuthService authService) {
        this.comidaUsuarioRepository = comidaRepository;
        this.comidaRepository = comidaRepository1;
        this.authService = authService;
    }

    @Transactional
    public ComidaResponseDTO salvar (ComidaDTO comidaDTO){
        ComidaUsuario comidaUsuario = new ComidaUsuario();
        comidaUsuario.setCalorias(comidaDTO.getCalorias());
        comidaUsuario.setNome(comidaDTO.getNome());
        comidaUsuario.setCarboidrato(comidaDTO.getCarboidrato());
        comidaUsuario.setGordura(comidaDTO.getGordura());
        comidaUsuario.setProteinas(comidaDTO.getProteinas());
        comidaUsuario.setIcone(comidaDTO.getIcone());
        Usuario usuario = authService.getUsuario();

        ComidaUsuario comparacao = comidaUsuarioRepository.findByNomeIgnoreCaseAndUsuario(comidaUsuario.getNome(),usuario)
                .orElse(null);

        if(comparacao != null){
            throw new RuntimeException("Comida já criada");
        }

        comidaUsuario.setUsuario(usuario);
        comidaUsuarioRepository.save(comidaUsuario);


        return new ComidaResponseDTO(comidaUsuario.getId(),
                comidaUsuario.getNome(),
                comidaUsuario.getCalorias(),
                comidaUsuario.getProteinas(),
                comidaUsuario.getCarboidrato(),
                comidaUsuario.getGordura(),
                comidaUsuario.getIcone());
    }

    @Transactional
    public void deletar(Long comidaUsuarioId) {
        ComidaUsuario comidaUsuario = comidaUsuarioRepository.findById(comidaUsuarioId).orElseThrow(
                () -> new RuntimeException("Comida  não encontrada")
        );

        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if(comidaUsuario.getUsuario().getId().equals(usuario.getId())) {
            comidaUsuarioRepository.delete(comidaUsuario);
        }
        else throw  new RuntimeException("Comida não pertence a esse usuário");
    }

    @Transactional
    public ComidaResponseDTO editar(Long id, ComidaDTO comida) {

            ComidaUsuario atualizado = comidaUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comida não encontrada"));
                if(comida.getNome()!= null && !comida.getNome().isEmpty() && !atualizado.getNome().equals(comida.getNome())) {
                    ComidaUsuario comparacao = comidaUsuarioRepository.findByNomeIgnoreCaseAndUsuario(comida.getNome(),
                            atualizado.getUsuario()).orElse(null);

                    if (comparacao != null) {
                        throw new RuntimeException("Comida já criada");
                    }
                }
        atualizado.setNome(comida.getNome());
        atualizado.setCarboidrato(comida.getCarboidrato());
        atualizado.setProteinas(comida.getProteinas());
        atualizado.setGordura(comida.getGordura());
        atualizado.setCalorias(comida.getCalorias());
        comidaUsuarioRepository.save(atualizado);
        return new ComidaResponseDTO(atualizado.getId(),
                atualizado.getNome(),
                atualizado.getCalorias(),
                atualizado.getProteinas(),
                atualizado.getCarboidrato(),
                atualizado.getGordura(),
                atualizado.getIcone());

    }


    public List<Comida> listarComida(){

        return comidaRepository.findAll();
    }

    public List<ComidaResponseDTO> listarComidaUsuario(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        List<ComidaUsuario> lista = comidaUsuarioRepository.findAllByUsuarioId(usuario.getId());

       return lista.stream()
                .map((comidaUsuario) -> new ComidaResponseDTO(comidaUsuario.getId(),
                        comidaUsuario.getNome(),
                        comidaUsuario.getCalorias(),
                        comidaUsuario.getProteinas(),
                        comidaUsuario.getCarboidrato(),
                        comidaUsuario.getGordura(),
                        comidaUsuario.getIcone()
                        ))
                .toList();
    }



}
