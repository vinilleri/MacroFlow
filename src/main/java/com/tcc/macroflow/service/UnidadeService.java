package com.tcc.macroflow.service;

import com.tcc.macroflow.model.Unidade;
import com.tcc.macroflow.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;

    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    public List<Unidade> listaUnidade(){
        return unidadeRepository.findAll();
    }

    public Unidade getUnidade(Long id){
        return unidadeRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Unidade não encontrada")
    );
    }
}
