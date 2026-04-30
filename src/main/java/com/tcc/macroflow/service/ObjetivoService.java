package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.ObjetivoDTO;
import com.tcc.macroflow.dto.ObjetivoResponseDTO;
import com.tcc.macroflow.model.Objetivo;
import com.tcc.macroflow.model.TipoObjetivo;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.ObjetivoRepository;
import com.tcc.macroflow.repository.TipoObjetivoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ObjetivoService {

    private final  ObjetivoRepository repository;
    private final TipoObjetivoRepository tipoObjetivoRepository;
    private final AuthService authService;
    public ObjetivoService(ObjetivoRepository repository, TipoObjetivoRepository tipoObjetivoRepository, AuthService authService) {
        this.repository = repository;
        this.tipoObjetivoRepository = tipoObjetivoRepository;
        this.authService = authService;
    }

    public List<TipoObjetivo> objetivos(){
        return tipoObjetivoRepository.findAll();
    }

    @Transactional
    public ObjetivoResponseDTO salvar(ObjetivoDTO dto){

        Objetivo objetivo = new Objetivo();
        Usuario usuario = authService.getUsuario();
        TipoObjetivo tipoObjetivo = tipoObjetivoRepository.findById(dto.getTipoObjetivoId()).orElseThrow(
                () -> new RuntimeException("Tipo do objetivo não encontrado")
        );
        objetivo.setTipoObjetivo(tipoObjetivo);
        objetivo.setUsuario(usuario);
        if(dto.getDataFim() != null) {
            objetivo.setDataFim(dto.getDataFim());
        }
        objetivo.setDataInicio(dto.getDataInicio());

        repository.save(objetivo);

        return new ObjetivoResponseDTO(objetivo.getDataInicio(),objetivo.getTipoObjetivo().getId());
    }

    @Transactional
    public ObjetivoResponseDTO atualizarObjetivo(ObjetivoDTO dto, Long objetivoId) {

        Objetivo atualizado = repository.findById(objetivoId).orElseThrow(
                () -> new RuntimeException("Objetivo não encontrado")
        );
        Usuario usuario = authService.getUsuario();

        if(atualizado.getUsuario().getId().equals(usuario.getId())) {
            TipoObjetivo tipoObjetivo = tipoObjetivoRepository.findById(dto.getTipoObjetivoId()).orElseThrow(
                    () -> new RuntimeException("Tipo do objetivo não encontrado")
            );
            atualizado.setTipoObjetivo(tipoObjetivo);
            atualizado.setUsuario(usuario);
            if (dto.getDataFim() != null) {
                atualizado.setDataFim(dto.getDataFim());
            }
            atualizado.setDataInicio(dto.getDataInicio());
            repository.save(atualizado);
            return new ObjetivoResponseDTO(atualizado.getDataInicio(), atualizado.getTipoObjetivo().getId());
        }
        throw  new RuntimeException("Objetivo não pertence a esse usuário");
    }

    }
