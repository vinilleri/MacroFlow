package com.tcc.macroflow.controller;


import com.tcc.macroflow.dto.EmailDTO;
import com.tcc.macroflow.dto.LogarDTO;
import com.tcc.macroflow.dto.UsuarioDTO;
import com.tcc.macroflow.model.CodigoEmail;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.service.EmailService;
import com.tcc.macroflow.service.TokenService;
import com.tcc.macroflow.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin("*")
public class UsuarioController {

private final UsuarioService usuarioService;
private final EmailService emailService;
private final TokenService tokenService;
    public UsuarioController(UsuarioService usuarioService, EmailService emailService, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }


    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody UsuarioDTO usuarioDTO){
        Usuario salvo = usuarioService.salvar(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping
    public ResponseEntity<?> editar( @RequestBody UsuarioDTO usuario){
        try{
            Usuario atualizado = usuarioService.editar(usuario);

            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deletar(){
    try {
        usuarioService.deletar();
        return ResponseEntity.noContent().build();
    }
    catch(Exception e){
        return ResponseEntity.notFound().build();
}
}
    @PostMapping("/login")
    public ResponseEntity<?> verificarLogin(@RequestBody LogarDTO logarDTO) throws Exception {
        if(usuarioService.verificarLogin(logarDTO.getEmail(),logarDTO.getSenha())){
            Usuario usuario = usuarioService.buscarPorEmail(logarDTO.getEmail());
            CodigoEmail ultimoCodigoEmail = emailService.buscarUltimo(usuario.getId());
            if(ultimoCodigoEmail != null && ultimoCodigoEmail.getDataCriacao().plusSeconds(10).isAfter(LocalDateTime.now())){
                throw new RuntimeException("Aguarde 10 segundos para solicitar outro código");
            }

                CodigoEmail codigoEmail = emailService.gerarCodigo(usuario);


                if (codigoEmail != null) {
                    emailService.enviarCodigo(usuario.getEmail(), codigoEmail);
                    return ResponseEntity.ok("Se o email existir, o código foi enviado");
                }
            }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    @PostMapping("/login/confirmar/{id}")
    public ResponseEntity<?> validarLogin(@PathVariable Long id, @RequestBody EmailDTO emailDTO) throws Exception {
        Usuario usuario = usuarioService.buscarPorId(id);

        emailService.validarCodigo(usuario, emailDTO.getCodigo());

        String token = tokenService.gerarToken(usuario);
        return ResponseEntity.ok(Collections.singletonMap("token",token));
    }

    @PostMapping("/deslogar")
    public ResponseEntity<?> deslogar() {

        usuarioService.deslogar();
        return ResponseEntity.ok("Usuário deslogado");
    }

}
