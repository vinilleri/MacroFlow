package com.tcc.macroflow.controller;

import com.tcc.macroflow.dto.ObjetivoDTO;
import com.tcc.macroflow.dto.ObjetivoResponseDTO;
import com.tcc.macroflow.model.Objetivo;
import com.tcc.macroflow.model.TipoObjetivo;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.service.ObjetivoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/objetivo")
@CrossOrigin("*")
public class ObjetivoController {
    private final ObjetivoService service;

    public ObjetivoController(ObjetivoService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<?> salvarObjetivo(@RequestBody  ObjetivoDTO dto){
        ObjetivoResponseDTO salvo =  service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarObjetivo(@RequestBody ObjetivoDTO dto, @PathVariable Long id){
        try {
            ObjetivoResponseDTO atualizado = service.atualizarObjetivo(dto, id);
            return ResponseEntity.ok().body(atualizado);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<TipoObjetivo>> listarObjetivos(){
        List<TipoObjetivo> lista = service.objetivos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/historico")
    public ResponseEntity<List<ObjetivoResponseDTO>> listarObjetivosAntigos(){
        List<ObjetivoResponseDTO> lista = service.listaObjetivosAntigos();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarObjetivoAntigo(@PathVariable Long id){
        try {
            service.deletarObjetivoAntigo(id);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<?> desativarObjetivo(@PathVariable Long id) {
        try {
            service.desativarObjetivo(id);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<ObjetivoResponseDTO> objetivoAtual(){
        ObjetivoResponseDTO atual = service.objetivoAtual();
        return  ResponseEntity.ok(atual);
    }

}
