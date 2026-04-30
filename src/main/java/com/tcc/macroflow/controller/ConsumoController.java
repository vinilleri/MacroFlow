package com.tcc.macroflow.controller;

import com.tcc.macroflow.dto.ConsumoItemDTO;
import com.tcc.macroflow.dto.MacroDTO;
import com.tcc.macroflow.service.ConsumoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/consumo")
@CrossOrigin("*")
public class ConsumoController{

    private final ConsumoService service;

    public ConsumoController(ConsumoService service) {
        this.service = service;
    }

    @GetMapping("/dia")
    public ResponseEntity<List<ConsumoItemDTO>> listarConsumoDia(){
        List<ConsumoItemDTO> consumoDia = service.listarConsumoDia();
        return ResponseEntity.ok(consumoDia);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<ConsumoItemDTO>> listarConsumoPeriodo(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime
                                                                     fim){
        List<ConsumoItemDTO> consumoDia = service.listarConsumoPeriodo(inicio,fim);
        return ResponseEntity.ok(consumoDia);
    }


    @GetMapping("/soma/dia")
    public ResponseEntity<MacroDTO> somarConsumoDia(){
        MacroDTO total = service.somarConsumoDia();
        return ResponseEntity.ok(total);
    }
    @GetMapping("/soma/periodo")
    public ResponseEntity<MacroDTO> somarConsumoDia(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime
            fim){
        MacroDTO total = service.somarConsumoPeriodo(inicio,fim);
        return ResponseEntity.ok(total);
    }



}
