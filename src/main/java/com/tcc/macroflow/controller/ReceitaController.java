package com.tcc.macroflow.controller;

import com.tcc.macroflow.dto.ReceitaDTO;
import com.tcc.macroflow.dto.ReceitaResponseDTO;
import com.tcc.macroflow.service.ReceitaService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receita")
@CrossOrigin("*")
public class ReceitaController {
    private final ReceitaService service;

    public ReceitaController(ReceitaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReceitaResponseDTO>> listarReceita(){
        List<ReceitaResponseDTO> lista = service.listarReceitaUsuario();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<?> salvarReceita(@RequestBody ReceitaDTO receitaDTO){

        ReceitaResponseDTO receita = service.salvar(receitaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(receita);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarReceita(@PathVariable Long id, @RequestBody ReceitaDTO receitaDTO){
        try{
           ReceitaResponseDTO atualizado= service.editar(receitaDTO,id);
            return ResponseEntity.ok(atualizado);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarReceita(@PathVariable Long id){
       try{
        service.deletarReceita(id);
        return ResponseEntity.noContent().build();
       }
        catch(Exception e){
          return ResponseEntity.notFound().build();
    }
    }





}
