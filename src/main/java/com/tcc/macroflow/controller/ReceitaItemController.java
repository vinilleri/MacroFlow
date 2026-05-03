package com.tcc.macroflow.controller;


import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.dto.ReceitaItemDTO;
import com.tcc.macroflow.dto.ReceitaItemRequestDTO;
import com.tcc.macroflow.service.ReceitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receita/{id}/itens")
@CrossOrigin("*")
public class ReceitaItemController {

    private final ReceitaService service;

    public ReceitaItemController(ReceitaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReceitaItemDTO>> listarItensReceita(@PathVariable Long id){
        List<ReceitaItemDTO> list = service.listarReceitaItem(id);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<?> salvarItensReceita(@RequestBody ReceitaItemRequestDTO dto, @PathVariable Long id) {
        ReceitaItemDTO itemDTO = service.adicionarItem(dto,id);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemDTO);
    }
    @PutMapping("/{itemId}")
    public ResponseEntity<?> editarItensReceita(@RequestBody ReceitaItemRequestDTO dto, @PathVariable Long itemId,
                                                @PathVariable Long id) {
        ReceitaItemDTO itemDTO = service.atualizarItem(dto,id,itemId);

        if(itemDTO == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itemDTO);
    }
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deletarItemReceita(@RequestParam Origem origem, @PathVariable Long itemId, @PathVariable Long id){
        service.deletarItem(origem,id,itemId);
        return ResponseEntity.noContent().build();
    }

}
