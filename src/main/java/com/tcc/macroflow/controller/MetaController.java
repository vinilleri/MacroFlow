package com.tcc.macroflow.controller;

import com.tcc.macroflow.dto.MetaDTO;
import com.tcc.macroflow.dto.MetaResponseDTO;
import com.tcc.macroflow.model.Meta;
import com.tcc.macroflow.service.MetaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meta")
@CrossOrigin("*")
public class MetaController {

    private final MetaService metaService;

    public MetaController(MetaService metaService) {
        this.metaService = metaService;
    }

    @PostMapping
    public ResponseEntity<?> criarMeta(@RequestBody MetaDTO dto){
        MetaResponseDTO salvo = metaService.criarMeta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<?> metaAtual(){
        MetaResponseDTO atual = metaService.buscarMetaAtual();
        return  ResponseEntity.ok(atual);
    }

    @GetMapping("/historico")
    public ResponseEntity<?> listarMetaAntiga(){
        List<MetaResponseDTO> lista = metaService.listarMetasAntigas();

        return  ResponseEntity.ok(lista);
    }

    @PatchMapping("/desativar")
    public ResponseEntity<?> desativarMetaAtual() {
        metaService.desativarMeta();

        return ResponseEntity.ok("Meta desativada");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarMetaAntiga(@PathVariable Long id) {
       try {
           metaService.deletarMetaAntiga(id);
           return ResponseEntity.noContent().build();
       }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> atualizarMetaAtual(@RequestBody MetaDTO dto) {
        try{
            MetaResponseDTO atualizado = metaService.atualizarMeta(dto);

            return ResponseEntity.ok(atualizado);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
