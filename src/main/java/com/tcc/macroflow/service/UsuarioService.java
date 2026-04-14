package com.tcc.macroflow.service;


import com.tcc.macroflow.dto.UsuarioDTO;
import com.tcc.macroflow.model.AtividadeFisica;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.AtividadeFisicaRepository;
import com.tcc.macroflow.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AtividadeFisicaRepository atividadeFisicaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, AtividadeFisicaRepository atividadeFisicaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.atividadeFisicaRepository = atividadeFisicaRepository;
    }

    @Transactional
    public Usuario salvar(UsuarioDTO dto) {

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());

        Usuario buscaEmail = usuarioRepository.findByEmail(dto.getEmail()).orElse(null);

        if(buscaEmail != null){
            throw  new RuntimeException("Email já existe");
        }
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));


        AtividadeFisica atividadeFisica = atividadeFisicaRepository.findById(dto.getAtividadeFisicaId())
                .orElseThrow(() -> new RuntimeException("Atividade física não encontrada"));
        usuario.setAtividadeFisica(atividadeFisica);


        return usuarioRepository.save(usuario);
    }
    @Transactional
    public Usuario editar(Long id, UsuarioDTO usuario) {

        Usuario atualizado = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));



        atualizado.setNome(usuario.getNome());
        if(usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            atualizado.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        if(usuario.getEmail() != null && !usuario.getEmail().isEmpty() && !atualizado.getEmail().equals(usuario.getEmail())) {
            Usuario buscaEmail = usuarioRepository.findByEmail(usuario.getEmail()).orElse(null);

            if (buscaEmail != null) {
                throw new RuntimeException("Email já existe");
            }
        }
        atualizado.setEmail(usuario.getEmail());

        AtividadeFisica atividadeFisica = atividadeFisicaRepository.findById(usuario.getAtividadeFisicaId())
                .orElseThrow(() -> new RuntimeException("Atividade física não encontrada"));

        atualizado.setAtividadeFisica(atividadeFisica);

        return usuarioRepository.save(atualizado);


    }
    @Transactional
    public void deletar(Long usuarioid) {
        Usuario usuario = usuarioRepository.findById(usuarioid).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        usuarioRepository.delete(usuario);
    }


    public boolean verificarLogin(String email, String senha) {

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("usuário não encontrado"));
        return passwordEncoder.matches(senha, usuario.getSenha());

    }

    public Usuario buscarPorId(Long usuarioID){
        return usuarioRepository.findById(usuarioID).orElseThrow(() -> new RuntimeException("usuario não encontrado"));
    }

    public void deslogar(Long usuarioID){
        Usuario usuario = usuarioRepository.findById(usuarioID).orElseThrow(() -> new RuntimeException("usuario não encontrado"));
        usuario.setAtivo(false);

        usuarioRepository.save(usuario);
    }
    public Usuario buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("usuario não encontrado"));
    }

}
