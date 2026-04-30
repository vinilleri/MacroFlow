package com.tcc.macroflow.service;

import com.tcc.macroflow.model.Unidade;
import com.tcc.macroflow.repository.UnidadeRepository;

import java.util.List;

public class UnidadeService {

    private final UnidadeRepository unidadeRepository;

    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    public List<Unidade> listaUnidade(){
        return unidadeRepository.findAll();
    }
}
