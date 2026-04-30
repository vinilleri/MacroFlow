package com.tcc.macroflow.service;

import com.tcc.macroflow.model.AtividadeFisica;
import com.tcc.macroflow.repository.AtividadeFisicaRepository;

import java.util.List;

public class AtividadeFisicaService {

    private final AtividadeFisicaRepository fisicaRepository;


    public AtividadeFisicaService(AtividadeFisicaRepository fisicaRepository) {
        this.fisicaRepository = fisicaRepository;
    }

    public List<AtividadeFisica> listaAtividadeFisica(){
        return fisicaRepository.findAll();
    }
}
