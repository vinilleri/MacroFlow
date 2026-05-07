package com.tcc.macroflow.service;

import com.tcc.macroflow.enums.Icone;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class IconeService {

    public List<Icone> listaIcones(){
       return new ArrayList<>(Arrays.asList(Icone.values()));
    }
}
