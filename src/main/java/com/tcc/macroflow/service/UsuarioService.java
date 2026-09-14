package com.tcc.macroflow.service;


import com.tcc.macroflow.dto.CompletudeDTO;
import com.tcc.macroflow.dto.UsuarioDTO;
import com.tcc.macroflow.helper.ValidarSenha;
import com.tcc.macroflow.model.AtividadeFisica;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AtividadeFisicaRepository atividadeFisicaRepository;
    private final ObjetivoRepository objetivoRepository;
    private final MedidasCorporaisRepository medidasCorporaisRepository;
    private final MetaRepository metaRepository;
    private  final AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, AtividadeFisicaRepository atividadeFisicaRepository, ObjetivoRepository objetivoRepository, MedidasCorporaisRepository medidasCorporaisRepository, MetaRepository metaRepository, AuthService authService) {
        this.usuarioRepository = usuarioRepository;
        this.atividadeFisicaRepository = atividadeFisicaRepository;
        this.objetivoRepository = objetivoRepository;
        this.medidasCorporaisRepository = medidasCorporaisRepository;
        this.metaRepository = metaRepository;
        this.authService = authService;
    }


    public Usuario usuarioAtual(){

        return  authService.getUsuario();
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
        ValidarSenha.validarSenha(dto.getSenha());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));


        AtividadeFisica atividadeFisica = atividadeFisicaRepository.findById(dto.getAtividadeFisicaId())
                .orElseThrow(() -> new RuntimeException("Atividade física não encontrada"));
        usuario.setAtividadeFisica(atividadeFisica);


        return usuarioRepository.save(usuario);
    }
    @Transactional
    public Usuario editar( UsuarioDTO usuario) {


        Usuario atualizado =  usuarioAtual();

        if(usuario.getNome() != null) {
            atualizado.setNome(usuario.getNome());
        }
        if(usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            atualizado.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        if(usuario.getEmail() != null && !usuario.getEmail().isEmpty() && !atualizado.getEmail().equals(usuario.getEmail())) {
            Usuario buscaEmail = usuarioRepository.findByEmail(usuario.getEmail()).orElse(null);

            if (buscaEmail != null) {
                throw new RuntimeException("Email já existe");
            }
            atualizado.setEmail(usuario.getEmail());
        }

        AtividadeFisica atividadeFisica = atividadeFisicaRepository.findById(usuario.getAtividadeFisicaId())
                .orElseThrow(() -> new RuntimeException("Atividade física não encontrada"));

        atualizado.setAtividadeFisica(atividadeFisica);

        return usuarioRepository.save(atualizado);


    }

    public Usuario mudarAtividadeFisica(Long atividadeFisicaId){
        Usuario atualizado = usuarioAtual();

        AtividadeFisica atividadeFisica = atividadeFisicaRepository.findById(atividadeFisicaId).orElseThrow(
                () -> new RuntimeException("Atividade física não encontrada")
        );

        atualizado.setAtividadeFisica(atividadeFisica);

        usuarioRepository.save(atualizado);

        return atualizado;


    }
    @Transactional
    public void deletar(){
        Usuario usuario = authService.getUsuario();
        usuarioRepository.delete(usuario);
    }

    public CompletudeDTO verificarCompletudeUsuario(){
     Usuario usuario = authService.getUsuario();

     return new CompletudeDTO(objetivoRepository.existsByUsuarioIdAndAtivo(usuario.getId(),true),
             medidasCorporaisRepository.existsByUsuarioId(usuario.getId()),
             metaRepository.existsByUsuarioId(usuario.getId())
     );
    }

    public boolean verificarLogin(String email, String senha) {

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("usuário não encontrado"));
        return passwordEncoder.matches(senha, usuario.getSenha());

    }

    public void deslogar(){
        Usuario usuario = authService.getUsuario();
        usuario.setAtivo(false);

        usuarioRepository.save(usuario);
    }
    public Usuario buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("usuario não encontrado"));
    }

}
