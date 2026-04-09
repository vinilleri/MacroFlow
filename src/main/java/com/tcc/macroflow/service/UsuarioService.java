package com.tcc.macroflow.service;


import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;

    }


    public Usuario salvar(Usuario usuario) {

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return usuarioRepository.save(usuario);
    }

    public Usuario editar(Long id, Usuario usuario) {

        Usuario atualizado = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));



        atualizado.setNome(usuario.getNome());
        if(usuario.getSenha() != null && usuario.getSenha().isEmpty()) {
            atualizado.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        atualizado.setEmail(usuario.getEmail());
        atualizado.setAtividadeFisica(usuario.getAtividadeFisica());

        return usuarioRepository.save(atualizado);


    }

    public void deletar(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }


    public boolean verificarLogin(String email, String senha) {

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("usuário não encontrado"));

        return passwordEncoder.matches(senha, usuario.getSenha());

    }


}
