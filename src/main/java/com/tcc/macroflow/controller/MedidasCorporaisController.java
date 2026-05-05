package com.tcc.macroflow.controller;

import com.tcc.macroflow.dto.MedidaCorporalDTO;
import com.tcc.macroflow.dto.MedidaCorporalResponseDTO;
import com.tcc.macroflow.service.MedidasCorporaisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/medidas-corporais")
@CrossOrigin("*")
public class MedidasCorporaisController{

    private final MedidasCorporaisService service;

    public MedidasCorporaisController(MedidasCorporaisService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> salvarMedidasCorporais(@RequestBody MedidaCorporalDTO dto){

        MedidaCorporalResponseDTO responseDTO = service.salvarMedida(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizarMedidasCorporais(@PathVariable  Long id, @RequestBody MedidaCorporalDTO dto){
        try {
            MedidaCorporalResponseDTO responseDTO = service.atualizarMedida(dto,id);

            return ResponseEntity.ok().body(responseDTO);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> buscarMedidasCorporaisAtuais(){
        MedidaCorporalResponseDTO atual =service.buscarMedidaAtual();

        return ResponseEntity.ok(atual);
    }

    @GetMapping("/periodo")
    public ResponseEntity<?> buscarMedidasCorporaisPeriodo(@RequestParam LocalDate inicio, @RequestParam LocalDate fim){
        List<MedidaCorporalResponseDTO> lista = service.buscarMedidaPorPeriodo(inicio,fim);
        return ResponseEntity.ok(lista);
    }

}
