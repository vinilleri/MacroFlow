package com.tcc.macroflow.service;

import com.tcc.macroflow.repository.MetaRepository;
import org.springframework.stereotype.Service;

@Service
public class MetaService {

    private final MetaRepository metaRepository;
    private final AuthService service;

    public MetaService(MetaRepository metaRepository, AuthService service) {
        this.metaRepository = metaRepository;
        this.service = service;
    }


}
