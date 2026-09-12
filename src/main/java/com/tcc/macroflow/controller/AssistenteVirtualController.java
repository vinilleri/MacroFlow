package com.tcc.macroflow.controller;


import com.tcc.macroflow.dto.AssistenteDTO;
import com.tcc.macroflow.dto.PerguntaDTO;
import com.tcc.macroflow.ia.service.EmbeddingService;
import com.tcc.macroflow.service.AssistenteVirtualService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assistente-virtual")
@CrossOrigin("*")
public class AssistenteVirtualController {
    private final AssistenteVirtualService assistenteVirtualService;
    private final EmbeddingService embeddingService;
    public AssistenteVirtualController(AssistenteVirtualService assistenteVirtualService, EmbeddingService embeddingService) {
        this.assistenteVirtualService = assistenteVirtualService;
        this.embeddingService = embeddingService;
    }

    @PostMapping
    public ResponseEntity<?> enviarMensagem(@RequestBody PerguntaDTO perguntaDTO){
        String response = assistenteVirtualService.enviarMensagem(perguntaDTO.getPergunta());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teste")
    public ResponseEntity<?> teste(){
        embeddingService.adicionarConheciomento();
        return  ResponseEntity.ok().build();
    }

    @GetMapping
    public  ResponseEntity<?> conversaAtual(){

        List<AssistenteDTO> response = assistenteVirtualService.listarMensagens();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deletarConversa(){
        assistenteVirtualService.deletarConversa();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
