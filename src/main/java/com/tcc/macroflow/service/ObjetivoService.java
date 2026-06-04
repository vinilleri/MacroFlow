package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.ObjetivoDTO;
import com.tcc.macroflow.dto.ObjetivoResponseDTO;
import com.tcc.macroflow.model.Objetivo;
import com.tcc.macroflow.model.TipoObjetivo;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.ObjetivoRepository;
import com.tcc.macroflow.repository.TipoObjetivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
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


    private ObjetivoResponseDTO converterEmDto(Objetivo objetivo){
       return new ObjetivoResponseDTO(objetivo.getId(),objetivo.getDataInicio(),objetivo.getDataFim(),objetivo.getTipoObjetivo());
    }
    @Transactional
    public ObjetivoResponseDTO salvar(ObjetivoDTO dto){

        Objetivo objetivo = new Objetivo();
        Usuario usuario = authService.getUsuario();

        Optional<Objetivo> objetivoAnterior = repository.findByUsuarioIdAndAtivo(usuario.getId(),true);

        if(objetivoAnterior.isPresent()){
            Objetivo antigoObjetivo = objetivoAnterior.get();

            if(antigoObjetivo.getDataFim() == null || antigoObjetivo.getDataFim().isAfter(LocalDate.now())){
                antigoObjetivo.setDataFim(LocalDate.now());
                antigoObjetivo.setAtivo(false);
                repository.save(antigoObjetivo);
            }
        }

        TipoObjetivo tipoObjetivo = tipoObjetivoRepository.findById(dto.getTipoObjetivoId()).orElseThrow(
                () -> new RuntimeException("Tipo do objetivo não encontrado")
        );
        objetivo.setTipoObjetivo(tipoObjetivo);
        objetivo.setUsuario(usuario);

        objetivo.setDataInicio(LocalDate.now());
        if(dto.getDataFim() != null) {
            if(dto.getDataFim().isBefore(objetivo.getDataInicio())) {
                throw new RuntimeException("Data final não pode ser antes do inicio");
            }
                objetivo.setDataFim(dto.getDataFim());

        }


        repository.save(objetivo);

        return converterEmDto(objetivo);
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
            repository.save(atualizado);
            return converterEmDto(atualizado);
        }
        throw  new RuntimeException("Objetivo não pertence a esse usuário");
    }

    public List<ObjetivoResponseDTO> listaObjetivosAntigos() {
        Usuario usuario = authService.getUsuario();
        List<Objetivo> lista = repository.findAllByUsuarioIdAndAtivo(usuario.getId(), false);

      return   lista.stream()
                .map(this:: converterEmDto)
                .toList();

    }

    @Transactional
    public void deletarObjetivoAntigo(Long objetivoId){
        Usuario usuario = authService.getUsuario();
        Objetivo objetivo = repository.findByUsuarioIdAndAtivoAndId(usuario.getId(), false,objetivoId).orElseThrow(
                () -> new RuntimeException("Objetivo ativo ou não encontrado")
        );
            repository.delete(objetivo);

    }



    public void desativarObjetivo(Long objetivoId){
        Objetivo objetivo = repository.findById(objetivoId).orElseThrow(
                () -> new RuntimeException("Objetivo não encontrado")
        );
        Usuario usuario = authService.getUsuario();
        if(objetivo.getUsuario().getId().equals(usuario.getId())) {
            if(!objetivo.isAtivo()){
                throw new RuntimeException("Objetivo já desativado");
            }
            objetivo.setAtivo(false);
            objetivo.setDataFim(LocalDate.now());
            repository.save(objetivo);
        }
        else throw  new RuntimeException("Objetivo não pertence a usuário");
    }

    public ObjetivoResponseDTO objetivoAtual(){
        Usuario usuario = authService.getUsuario();
        Objetivo objetivo = repository.findByUsuarioIdAndAtivo(usuario.getId(), true).orElseThrow(
                () -> new RuntimeException("Erro em achar objetivo atual")
        );

        return converterEmDto(objetivo);
    }

    }
