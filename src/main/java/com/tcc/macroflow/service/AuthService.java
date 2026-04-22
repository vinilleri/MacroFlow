package com.tcc.macroflow.service;

import com.tcc.macroflow.model.Usuario;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Usuario getUsuario(){
        return (Usuario) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
