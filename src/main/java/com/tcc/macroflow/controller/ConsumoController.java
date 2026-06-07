package com.tcc.macroflow.controller;

import com.tcc.macroflow.dto.ConsumoItemDTO;
import com.tcc.macroflow.dto.ConsumoResultanteDTO;
import com.tcc.macroflow.dto.MacroDTO;
import com.tcc.macroflow.model.Consumo;
import com.tcc.macroflow.service.ConsumoService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<Consumo>> listarConsumoDia(){
        List<Consumo> consumoDia = service.listarConsumoDia();
        return ResponseEntity.ok(consumoDia);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Consumo>> listarConsumoPeriodo(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime
                                                                     fim){
        List<Consumo> consumoDia = service.listarConsumoPeriodo(inicio,fim);
        return ResponseEntity.ok(consumoDia);
    }


    @PostMapping
    public ResponseEntity<?> consumirItem(@RequestBody ConsumoItemDTO consumoItemDTO){

        Consumo salvo = service.consumirItem(consumoItemDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarConsumo(@RequestBody ConsumoItemDTO consumoItemDTO, @PathVariable Long id){

        try{
            Consumo atualizado = service.editarConsumo(consumoItemDTO, id);

            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public  ResponseEntity<?> buscarConsumo(@PathVariable Long id){
        try {
            Consumo consumo = service.buscarConsumo(id);
            return ResponseEntity.ok(consumo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarConsumo(@PathVariable Long id) {
        try{
            service.deletarConsumo(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
