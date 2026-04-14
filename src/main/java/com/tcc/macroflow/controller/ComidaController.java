package com.tcc.macroflow.controller;


import com.tcc.macroflow.dto.ComidaDTO;
import com.tcc.macroflow.dto.UsuarioDTO;
import com.tcc.macroflow.model.Comida;
import com.tcc.macroflow.model.ComidaUsuario;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.service.ComidaService;
import com.tcc.macroflow.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comida")
@CrossOrigin("*")
public class ComidaController {

    private final ComidaService comidaService;
    private final UsuarioService usuarioService;
    public ComidaController(ComidaService comidaService, UsuarioService usuarioService) {
        this.comidaService = comidaService;
        this.usuarioService = usuarioService;
    }
//  @PreAuthorize("#id == authentication.principal.id") adicionar isso depois dos testes!!
    @GetMapping("/usuario/{usuarioId}/comidas")
    public ResponseEntity<List<ComidaUsuario>> listarComidasDoUsuario(@PathVariable Long usuarioId){
        List<ComidaUsuario> lista = comidaService.listarComidaUsuario(usuarioId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping
    public ResponseEntity<List<Comida>> listarComidas(){
        List<Comida> lista = comidaService.listarComida();
        return ResponseEntity.ok(lista);
    }
    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ComidaDTO comida){

        ComidaUsuario salvo = comidaService.salvar(comida);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
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
            ComidaUsuario atualizado = comidaService.editar(id, dto);

            return ResponseEntity.ok(atualizado);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }


}
