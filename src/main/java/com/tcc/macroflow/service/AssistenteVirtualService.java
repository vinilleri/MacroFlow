package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.AssistenteDTO;

import com.tcc.macroflow.ia.service.MacroFlowIaService;
import com.tcc.macroflow.model.AssistenteVirtual;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.AssistenteVirtualRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssistenteVirtualService {
    private final AssistenteVirtualRepository assistenteVirtualRepository;
    private final AuthService authService;
    private final MacroFlowIaService macroFlowIaService;


    public AssistenteVirtualService(AssistenteVirtualRepository assistenteVirtualRepository,
                                    AuthService authService,MacroFlowIaService macroFlowIaService) {
        this.assistenteVirtualRepository = assistenteVirtualRepository;
        this.authService = authService;
        this.macroFlowIaService = macroFlowIaService;
    }
    private AssistenteDTO transformarDto(AssistenteVirtual virtual){
        return new AssistenteDTO(virtual.getMensagemUsuario(), virtual.getMensagemIa() );
    }

    @Transactional
    public String enviarMensagem(String pergunta){
        Usuario usuario = authService.getUsuario();
        AssistenteVirtual assistenteVirtual = new AssistenteVirtual();
        assistenteVirtual.setUsuario(usuario);
        assistenteVirtual.setMensagemUsuario(pergunta);
        String resposta = macroFlowIaService.responder(pergunta);
        assistenteVirtual.setMensagemIa(resposta);
        assistenteVirtual.setDataHora(LocalDateTime.now());

        assistenteVirtualRepository.save(assistenteVirtual);

        return assistenteVirtual.getMensagemIa();
    }

public List<AssistenteDTO> listarMensagens() {
    Usuario usuario = authService.getUsuario();

    List<AssistenteVirtual> lista = assistenteVirtualRepository.findAllByUsuarioIdOrderByDataHoraAsc(usuario.getId());

    return lista.stream()
            .map(this::transformarDto)
            .toList();
}

@Transactional
public void deletarConversa(){
        Usuario usuario = authService.getUsuario();
        assistenteVirtualRepository.deleteByUsuarioId(usuario.getId());
}




}
