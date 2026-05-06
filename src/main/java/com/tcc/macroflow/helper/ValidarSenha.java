package com.tcc.macroflow.helper;

public class ValidarSenha {

    public static void validarSenha(String senha){

       if(senha.length() < 8){
           throw new RuntimeException("Senha não pode ter menos de 8 caractéres");
       }

       if(!senha.matches(".*[a-z].* ") || !senha.matches(".*[A-Z].* ")){
           throw new RuntimeException("Senha precisa de caractéres minúsculos e maiúsculos");
       }

       if(!senha.matches(".*\\d.*")){
           throw new RuntimeException("Senha precisa de números");
       }
        if(!senha.matches(".*[@#$%^&+=!].*")){
            throw new RuntimeException("Senha precisa de caractéres especiais");
        }


    }
}
