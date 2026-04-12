package com.tcc.macroflow.service;

import com.tcc.macroflow.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String chave;


    public String gerarToken(Usuario usuario){

        return Jwts.builder()
                .subject(usuario.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor(chave.getBytes()))
                .compact();
    }

    public String validarToken(String token){
        try{
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(chave.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }
        catch(Exception e){
            System.out.println("Erro na validação do token: " + e.getMessage());
           return null;

        }
    }

}
