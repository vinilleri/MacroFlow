package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.MedidaCorporalDTO;
import com.tcc.macroflow.dto.MedidaCorporalResponseDTO;
import com.tcc.macroflow.model.MedidasCorporais;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.MedidasCorporaisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedidasCorporaisService {

    private final MedidasCorporaisRepository repository;
    private final AuthService authService;
    public MedidasCorporaisService(MedidasCorporaisRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    @Transactional
    public MedidaCorporalResponseDTO salvarMedida(MedidaCorporalDTO dto){
        MedidasCorporais salvo = new MedidasCorporais();
        Usuario usuario = authService.getUsuario();
        salvo.setAltura(dto.getAltura());
        salvo.setData(dto.getData());
        salvo.setUsuario(usuario);
        salvo.setPercentualGordura(dto.getPercentualGordura());
        salvo.setCircuferenciaCintura(dto.getCircuferenciaCintura());
        salvo.setAltura(dto.getAltura());

        return new MedidaCorporalResponseDTO(salvo.getPeso(),salvo.getAltura(),salvo.getPercentualGordura(),salvo.getCircuferenciaCintura()
        ,salvo.getData());
    }

    @Transactional
    public MedidaCorporalResponseDTO atualizarMedida(MedidaCorporalDTO dto, Long id){
        MedidasCorporais atualizado = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Medidas Corporais não  foram encontradas")
        );
        Usuario usuario = authService.getUsuario();


        if(atualizado.getUsuario().getId().equals(usuario.getId())) {
            atualizado.setAltura(dto.getAltura());
            atualizado.setData(dto.getData());
            atualizado.setUsuario(usuario);
            atualizado.setPercentualGordura(dto.getPercentualGordura());
            atualizado.setCircuferenciaCintura(dto.getCircuferenciaCintura());
            atualizado.setAltura(dto.getAltura());

            return new MedidaCorporalResponseDTO(atualizado.getPeso(), atualizado.getAltura(), atualizado.getPercentualGordura(), atualizado.getCircuferenciaCintura()
                    , atualizado.getData());
        }
        throw  new RuntimeException("Medidas Corporais não pertence a esse usuário");
    }

}
