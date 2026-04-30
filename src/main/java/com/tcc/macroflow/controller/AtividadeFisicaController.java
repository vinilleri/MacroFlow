package com.tcc.macroflow.controller;

import com.tcc.macroflow.model.AtividadeFisica;
import com.tcc.macroflow.service.AtividadeFisicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/atividadeFisica")
@CrossOrigin("*")
public class AtividadeFisicaController {

    private final AtividadeFisicaService service;

    public AtividadeFisicaController(AtividadeFisicaService service) {
        this.service = service;
    }

    public ResponseEntity<List<AtividadeFisica>> listarAtividadeFisica(){
        List<AtividadeFisica> lista = service.listaAtividadeFisica();
        return ResponseEntity.ok(lista);
    }
}
