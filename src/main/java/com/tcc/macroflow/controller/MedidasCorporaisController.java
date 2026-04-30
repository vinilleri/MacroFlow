package com.tcc.macroflow.controller;

import com.tcc.macroflow.dto.MedidaCorporalDTO;
import com.tcc.macroflow.dto.MedidaCorporalResponseDTO;
import com.tcc.macroflow.service.MedidasCorporaisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medidasCorporais")
@CrossOrigin("*")
public class MedidasCorporaisController{

    private final MedidasCorporaisService service;

    public MedidasCorporaisController(MedidasCorporaisService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> salvarMedidasCorporais(MedidaCorporalDTO dto){

        MedidaCorporalResponseDTO responseDTO = service.salvarMedida(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> salvarMedidasCorporais(Long id,MedidaCorporalDTO dto){
        try {
            MedidaCorporalResponseDTO responseDTO = service.atualizarMedida(dto,id);

            return ResponseEntity.ok().body(responseDTO);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
