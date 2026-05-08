package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.MedidaCorporalDTO;
import com.tcc.macroflow.dto.MedidaCorporalResponseDTO;
import com.tcc.macroflow.model.MedidasCorporais;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.MedidasCorporaisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedidasCorporaisService {

    private final MedidasCorporaisRepository repository;
    private final AuthService authService;
    public MedidasCorporaisService(MedidasCorporaisRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }
    private MedidaCorporalResponseDTO converterParaResponseDTO(MedidasCorporais medida) {
        return new MedidaCorporalResponseDTO(
                medida.getId(),
                medida.getPeso(),
                medida.getAltura(),
                medida.getPercentualGordura(),
                medida.getCircuferenciaCintura(),
                medida.getDataNascimento(),
                medida.getSexo(),
                medida.getData()
        );
    }

    @Transactional
    public MedidaCorporalResponseDTO salvarMedida(MedidaCorporalDTO dto){
        MedidasCorporais salvo = new MedidasCorporais();
        Usuario usuario = authService.getUsuario();
        salvo.setPeso(dto.getPeso());
        salvo.setAltura(dto.getAltura());
        salvo.setData(LocalDate.now());
        salvo.setUsuario(usuario);
        if(dto.getPercentualGordura() != null) {
            salvo.setPercentualGordura(dto.getPercentualGordura());
        }
        if(dto.getCircuferenciaCintura() != null) {
            salvo.setCircuferenciaCintura(dto.getCircuferenciaCintura());
        }
        salvo.setSexo(dto.getSexo());
        salvo.setDataNascimento(dto.getDataNascimento());
        salvo.setAltura(dto.getAltura());

        repository.save(salvo);
        return converterParaResponseDTO(salvo);

    }

    @Transactional
    public MedidaCorporalResponseDTO atualizarMedida(MedidaCorporalDTO dto, Long id){
        MedidasCorporais atualizado = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Medidas Corporais não  foram encontradas")
        );
        Usuario usuario = authService.getUsuario();


        if(atualizado.getUsuario().getId().equals(usuario.getId())) {
            atualizado.setAltura(dto.getAltura());
            atualizado.setData(LocalDate.now());
            atualizado.setUsuario(usuario);
            atualizado.setPeso(dto.getPeso());
            if(dto.getPercentualGordura() != null) {
                atualizado.setPercentualGordura(dto.getPercentualGordura());
            }
            if(dto.getCircuferenciaCintura() != null) {
                atualizado.setCircuferenciaCintura(dto.getCircuferenciaCintura());
            }
            atualizado.setSexo(dto.getSexo());
            atualizado.setDataNascimento(dto.getDataNascimento());
            atualizado.setAltura(dto.getAltura());
            repository.save(atualizado);

            return converterParaResponseDTO(atualizado);
        }
        throw  new RuntimeException("Medidas Corporais não pertence a esse usuário");
    }

    public List<MedidaCorporalResponseDTO> buscarMedidaPorPeriodo(LocalDate inicio, LocalDate fim){
        Usuario usuario = authService.getUsuario();

        if(inicio.isAfter(fim)){
            throw new RuntimeException("Data inválida! O começo do período não pode vir depois do fim");
        }
       List <MedidasCorporais> medidasCorporaisList = repository.findAllByUsuarioIdAndDataBetween(usuario.getId()
               , inicio,fim);

      return  medidasCorporaisList.stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }




    public MedidaCorporalResponseDTO buscarMedidaAtual(){
        Usuario usuario = authService.getUsuario();
      MedidasCorporais atual =  repository.findTopByUsuarioIdOrderByDataDesc(usuario.getId()).orElseThrow(
              () -> new RuntimeException("Medidas Corporais não  foram encontradas")
      );

      return converterParaResponseDTO(atual);
    }




}
