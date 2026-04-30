package com.tcc.macroflow.controller;


import com.tcc.macroflow.model.Unidade;
import com.tcc.macroflow.service.UnidadeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
