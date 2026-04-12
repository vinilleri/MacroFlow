package com.tcc.macroflow.dto;


import lombok.Data;

@Data
public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private Long atividadeFisicaId;

}
