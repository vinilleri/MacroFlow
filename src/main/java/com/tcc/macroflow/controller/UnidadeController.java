package com.tcc.macroflow.controller;


import com.tcc.macroflow.model.Unidade;
import com.tcc.macroflow.service.UnidadeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidade")
@CrossOrigin("*")
public class UnidadeController {
    private final UnidadeService unidadeService;

    public UnidadeController(UnidadeService unidadeService) {
        this.unidadeService = unidadeService;
    }


    @GetMapping
    public ResponseEntity<List<Unidade>> listarUnidade(){
        List<Unidade> lista =  unidadeService.listaUnidade();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unidade> buscarUnidade(@PathVariable Long id){
       try {
           Unidade unidade = unidadeService.getUnidade(id);
           return ResponseEntity.ok(unidade);
       }
       catch(Exception e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

       }
    }


}
