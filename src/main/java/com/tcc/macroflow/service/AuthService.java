package com.tcc.macroflow.service;

import com.tcc.macroflow.model.Usuario;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Usuario getUsuario(){
        Object objeto = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if(objeto instanceof Usuario usuario){
            return usuario;
        }
        throw new RuntimeException("Usuario não autenticado");
    }
}
