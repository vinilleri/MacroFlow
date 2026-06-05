package com.tcc.macroflow.controller;

import com.tcc.macroflow.dto.ConsumoReceitaResponseDTO;
import com.tcc.macroflow.enums.Origem;
import com.tcc.macroflow.dto.ConsumoComidaDTO;
import com.tcc.macroflow.dto.ConsumoComidaResponseDTO;
import com.tcc.macroflow.service.ConsumoComidaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumo/consumo-comida")
@CrossOrigin("*")
public class ConsumoComidaController {

    private final ConsumoComidaService consumoComidaService;


    public ConsumoComidaController(ConsumoComidaService consumoComidaService) {
        this.consumoComidaService = consumoComidaService;
    }

    @PostMapping
    public ResponseEntity<?> consumirComida(@RequestBody ConsumoComidaDTO dto){
       ConsumoComidaResponseDTO salvo = consumoComidaService.consumirComida(dto);
       return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
    @GetMapping
    public ResponseEntity<?> listarConsumoComida(){
        List<ConsumoComidaResponseDTO> lista = consumoComidaService.listarConsumosComida();
        return ResponseEntity.ok(lista);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> pegarConsumoComida(@PathVariable Long id, @RequestParam Origem origem){
        ConsumoComidaResponseDTO response = consumoComidaService.getComida(id,origem);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarConsumoComida(@PathVariable Long id, @RequestBody ConsumoComidaDTO dto) {

        try{
            ConsumoComidaResponseDTO atualizado = consumoComidaService.atualizarConsumoComida(dto,id);

            return ResponseEntity.ok(atualizado);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        }

      @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarConsumoComida(@PathVariable Long id,
      @RequestParam Origem origem) {

          try{
             consumoComidaService.deletarConsumoComida(origem,id);

              return ResponseEntity.noContent().build();
          }
          catch(Exception e){
              return ResponseEntity.notFound().build();
          }
      }

}
