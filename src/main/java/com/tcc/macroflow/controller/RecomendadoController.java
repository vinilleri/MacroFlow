package com.tcc.macroflow.controller;

import com.tcc.macroflow.dto.AlimentoRecomendadoDTO;
import com.tcc.macroflow.service.RecomendacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/recomendado")
@CrossOrigin("*")
public class RecomendadoController {
    private final RecomendacaoService service;

    public RecomendadoController(RecomendacaoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> receberRecomendados(){
        ArrayList<AlimentoRecomendadoDTO> alimentos = service.alimentosRecomendados();
        return  ResponseEntity.ok(alimentos);
    }
}
