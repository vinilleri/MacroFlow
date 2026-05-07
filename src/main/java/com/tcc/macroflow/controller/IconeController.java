package com.tcc.macroflow.controller;

import com.tcc.macroflow.enums.Icone;
import com.tcc.macroflow.model.AtividadeFisica;
import com.tcc.macroflow.service.AtividadeFisicaService;
import com.tcc.macroflow.service.IconeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/icone")
@CrossOrigin("*")
public class IconeController {

    private final IconeService service;

    public IconeController(IconeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Icone>> listarIcones(){
        List<Icone> lista = service.listaIcones();
        return ResponseEntity.ok(lista);
    }
}
