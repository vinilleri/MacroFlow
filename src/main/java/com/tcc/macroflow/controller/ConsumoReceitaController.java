package com.tcc.macroflow.controller;

import com.tcc.macroflow.dto.ConsumoReceitaDTO;
import com.tcc.macroflow.dto.ConsumoReceitaResponseDTO;
import com.tcc.macroflow.service.ConsumoReceitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumo/consumo-receita")
@CrossOrigin("*")
public class ConsumoReceitaController {

        private final ConsumoReceitaService consumoReceitaService;


        public ConsumoReceitaController(ConsumoReceitaService consumoReceitaService) {
            this.consumoReceitaService = consumoReceitaService;
        }

        @PostMapping
        public ResponseEntity<?> consumirReceita(@RequestBody ConsumoReceitaDTO dto){
            ConsumoReceitaResponseDTO salvo = consumoReceitaService.consumirReceita(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        }


        @PutMapping("/{id}")
        public ResponseEntity<?> atualizarConsumoReceita(@PathVariable Long id, @RequestBody ConsumoReceitaDTO dto) {
            try{
                ConsumoReceitaResponseDTO atualizado = consumoReceitaService.atualizarConsumirReceita(dto,id);
                return ResponseEntity.ok(atualizado);
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }

        @GetMapping
        public ResponseEntity<?> listarConsumoReceita(){
            List<ConsumoReceitaResponseDTO> lista = consumoReceitaService.listarConsumoReceita();

            return ResponseEntity.ok(lista);
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> pegarConsumoReceita(@PathVariable Long id){
            ConsumoReceitaResponseDTO response = consumoReceitaService.getConsumoReceita(id);

            return ResponseEntity.ok(response);
        }


        @DeleteMapping("/{id}")
        public ResponseEntity<?> deletarConsumoReceita(@PathVariable Long id) {

            try{
                consumoReceitaService.deletarConsumoReceita(id);

                return ResponseEntity.noContent().build();
            }
            catch(Exception e){
                return ResponseEntity.notFound().build();
            }
        }

    }


