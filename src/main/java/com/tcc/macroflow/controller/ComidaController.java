package com.tcc.macroflow.controller;


import com.tcc.macroflow.dto.ComidaDTO;

import com.tcc.macroflow.dto.ComidaResponseDTO;
import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.model.Comida;


import com.tcc.macroflow.service.ComidaService;
import com.tcc.macroflow.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comida")
@CrossOrigin("*")
public class ComidaController {

    private final ComidaService comidaService;

    public ComidaController(ComidaService comidaService, UsuarioService usuarioService) {
        this.comidaService = comidaService;

    }


    @GetMapping("/usuario")
    public ResponseEntity<List<ComidaResponseDTO>> listarComidasDoUsuario(){
        List<ComidaResponseDTO> lista = comidaService.listarComidaUsuario();
        return ResponseEntity.ok(lista);
    }


    @GetMapping("/todos")
    public ResponseEntity<List<ComidaResponseDTO>> listarComidasGerais(){
        List<ComidaResponseDTO> lista = comidaService.listarComidas();
        return ResponseEntity.ok(lista);
    }

    @GetMapping
    public ResponseEntity<List<Comida>> listarComidas(){
        List<Comida> lista = comidaService.listarComida();
        return ResponseEntity.ok(lista);
    }
    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ComidaDTO comida){
       ComidaResponseDTO dto = comidaService.salvar(comida);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id){


        try {
            comidaService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }






    @PutMapping("/{id}")

    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody ComidaDTO dto){
        try {
            ComidaResponseDTO atualizado = comidaService.editar(id, dto);

            return ResponseEntity.ok(atualizado);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarComida(@PathVariable Long id, @RequestParam Origem origem){

        try{
            ComidaResponseDTO response = comidaService.getComida(id,origem);

            return  ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
