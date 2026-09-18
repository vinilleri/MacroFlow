package com.tcc.macroflow.service;

import com.tcc.macroflow.helper.ValidarSenha;
import com.tcc.macroflow.model.CodigoEmail;
import com.tcc.macroflow.model.TokenRecuperacao;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.EmailRepository;
import com.tcc.macroflow.repository.TokenRecuperacaoRepository;
import com.tcc.macroflow.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class EmailService
{

    private final EmailRepository emailRepository;
    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacaoRepository tokenRecuperacaoRepository;
    @Value("${resend.api.key}")
    private String apiKey;
    private static final SecureRandom random = new SecureRandom();
    @Autowired
    private PasswordEncoder passwordEncoder;

    public EmailService(EmailRepository emailRepository, UsuarioRepository usuarioRepository, TokenRecuperacaoRepository tokenRecuperacaoRepository) {
        this.emailRepository = emailRepository;
        this.usuarioRepository = usuarioRepository;
        this.tokenRecuperacaoRepository = tokenRecuperacaoRepository;
    }

    public  CodigoEmail gerarCodigo (Usuario usuario){
        CodigoEmail novoCodigo = new CodigoEmail();

        String codigo = String.valueOf(random.nextInt(900000)+ 100000);
        novoCodigo.setCodigo(codigo);
        novoCodigo.setDataExpiracao(LocalDateTime.now().plusMinutes(10));
        novoCodigo.setDataCriacao(LocalDateTime.now());
        novoCodigo.setUsado(false);
        novoCodigo.setUsuario(usuario);

        return emailRepository.save(novoCodigo);



    }

    public TokenRecuperacao gerarToken(String email){
        TokenRecuperacao tokenRecuperacao = new TokenRecuperacao();
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado")
        );
        TokenRecuperacao tokenAntigo = tokenRecuperacaoRepository.findTopByUsuarioIdOrderByDataCriacaoDesc(usuario.getId()).orElse(null);
        if(tokenAntigo != null){
            tokenAntigo.setUsado(true);
            tokenRecuperacaoRepository.save(tokenAntigo);
        }

        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        tokenRecuperacao.setCodigo(token);
        tokenRecuperacao.setDataExpiracao(LocalDateTime.now().plusMinutes(10));
        tokenRecuperacao.setDataCriacao(LocalDateTime.now());
        tokenRecuperacao.setUsado(false);
        tokenRecuperacao.setUsuario(usuario);

        return tokenRecuperacaoRepository.save(tokenRecuperacao);
    }

    public void enviarCodigo(String destino, CodigoEmail codigoEmail) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String html = """
<div style="
  margin:0;
  padding:40px 20px;
  background:#edf7ee;
  font-family:Arial, Helvetica, sans-serif;
  color:#1f2d25;
">

  <div style="
    max-width:480px;
    margin:0 auto;
    background:rgba(255,255,255,0.92);
    border:1px solid #dbe7dd;
    border-radius:24px;
    overflow:hidden;
    box-shadow:0 18px 50px rgba(44,80,57,0.14);
  ">

    <div style="
      height:7px;
      background:linear-gradient(
        90deg,
        #315b41,
        #8fba91,
        #dfeee2,
        #6f9d72,
        #315b41
      );
    "></div>

    <div style="padding:38px 36px 34px;">

      <!-- Logo -->
      <div style="
        text-align:center;
        margin-bottom:30px;
      ">
        <div style="
          display:inline-block;
          padding:12px 20px;
          border-radius:18px;
          background:linear-gradient(
            145deg,
            #ffffff,
            #edf7ee
          );
          border:1px solid #dbe7dd;
          box-shadow:
            inset 0 1px 0 rgba(255,255,255,0.95),
            0 8px 22px rgba(44,80,57,0.08);
        ">
          <span style="
            font-size:22px;
            font-weight:800;
            letter-spacing:-0.5px;
            color:#315b41;
          ">
            MacroFlow
          </span>
        </div>
      </div>

      <!-- Título -->
      <div style="text-align:center;">

        <div style="
          display:inline-block;
          padding:6px 12px;
          margin-bottom:14px;
          border-radius:999px;
          background:#dfeee2;
          color:#315b41;
          font-size:11px;
          font-weight:bold;
          letter-spacing:1px;
          text-transform:uppercase;
        ">
          Verificação de conta
        </div>

        <h2 style="
          margin:0;
          font-size:28px;
          line-height:1.2;
          color:#1f2d25;
        ">
          Confirme seu acesso.
        </h2>

        <p style="
          margin:16px 0 0;
          color:#718078;
          font-size:15px;
          line-height:1.6;
        ">
          Use o código abaixo para confirmar
          o acesso à sua conta MacroFlow.
        </p>

      </div>

      <!-- Código -->
      <div style="
        text-align:center;
        margin:30px 0;
      ">
        <div style="
          display:inline-block;
          padding:18px 28px;
          border-radius:16px;
          background:linear-gradient(
            145deg,
            #ffffff,
            #edf7ee
          );
          border:1px solid #dbe7dd;
          box-shadow:
            inset 0 1px 0 rgba(255,255,255,0.95),
            0 10px 26px rgba(44,80,57,0.10);
        ">
          <span style="
            font-size:34px;
            font-weight:800;
            letter-spacing:7px;
            color:#315b41;
          ">
            """ + codigoEmail.getCodigo() + """
          </span>
        </div>
      </div>

      <!-- Aviso -->
      <div style="
        padding:16px 18px;
        border-radius:14px;
        background:#f5faf6;
        border:1px solid #dbe7dd;
      ">

        <p style="
          margin:0;
          color:#718078;
          font-size:13px;
          line-height:1.5;
          text-align:center;
        ">
          Este código é válido por <strong style="color:#315b41;">
          10 minutos
          </strong> e poderá ser utilizado apenas uma vez.
        </p>

      </div>

      <p style="
        margin:24px 0 0;
        color:#9aa69f;
        font-size:12px;
        line-height:1.5;
        text-align:center;
      ">
        Se você não solicitou este código,
        pode ignorar este e-mail com segurança.
      </p>

    </div>

    <!-- Rodapé -->
    <div style="
      padding:18px;
      background:#f5faf6;
      border-top:1px solid #dbe7dd;
      text-align:center;
    ">
      <span style="
        color:#718078;
        font-size:11px;
      ">
        © MacroFlow · Seu progresso em fluxo.
      </span>
    </div>

  </div>

</div>
""";

        String body = """
    {
      "from": "MacroFlow <noreply@macroflow.app.br>",
      "to": ["%s"],
      "subject": "Seu código de verificação · MacroFlow",
      "html": "%s"
    }
    """.formatted(
                destino,
                html.replace("\"", "\\\"").replace("\n", "")
        );

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(
                "https://api.resend.com/emails",
                request,
                String.class
        );
    }

    public void enviarURL(String destino, TokenRecuperacao tokenRecuperacao) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String html = """
<div style="
  margin:0;
  padding:40px 20px;
  background:#edf7ee;
  font-family:Arial, Helvetica, sans-serif;
  color:#1f2d25;
">

  <div style="
    max-width:480px;
    margin:0 auto;
    background:rgba(255,255,255,0.92);
    border:1px solid #dbe7dd;
    border-radius:24px;
    overflow:hidden;
    box-shadow:0 18px 50px rgba(44,80,57,0.14);
  ">

    <!-- Brilho superior -->
    <div style="
      height:7px;
      background:linear-gradient(
        90deg,
        #315b41,
        #8fba91,
        #dfeee2,
        #6f9d72,
        #315b41
      );
    "></div>

    <div style="padding:38px 36px 34px;">

      <!-- Logo -->
      <div style="
        text-align:center;
        margin-bottom:30px;
      ">
        <div style="
          display:inline-block;
          padding:12px 20px;
          border-radius:18px;
          background:linear-gradient(
            145deg,
            #ffffff,
            #edf7ee
          );
          border:1px solid #dbe7dd;
          box-shadow:
            inset 0 1px 0 rgba(255,255,255,0.95),
            0 8px 22px rgba(44,80,57,0.08);
        ">
          <span style="
            font-size:22px;
            font-weight:800;
            letter-spacing:-0.5px;
            color:#315b41;
          ">
            MacroFlow
          </span>
        </div>
      </div>

      <!-- Título -->
      <div style="text-align:center;">

        <div style="
          display:inline-block;
          padding:6px 12px;
          margin-bottom:14px;
          border-radius:999px;
          background:#dfeee2;
          color:#315b41;
          font-size:11px;
          font-weight:bold;
          letter-spacing:1px;
          text-transform:uppercase;
        ">
          Recuperação de acesso
        </div>

        <h2 style="
          margin:0;
          font-size:28px;
          line-height:1.2;
          color:#1f2d25;
        ">
          Vamos recuperar<br>seu acesso.
        </h2>

        <p style="
          margin:16px 0 0;
          color:#718078;
          font-size:15px;
          line-height:1.6;
        ">
          Recebemos uma solicitação para redefinir
          a senha da sua conta MacroFlow.
        </p>

      </div>

      <!-- Botão -->
      <div style="
        text-align:center;
        margin:30px 0;
      ">

        <a href="http://macroflow.app.br/paginas/recuperarSenha.html?token=""" + tokenRecuperacao.getCodigo() + """
        " style="
          display:inline-block;
          padding:15px 28px;
          border-radius:14px;
          background:linear-gradient(
            135deg,
            #315b41,
            #6f9d72
          );
          color:#ffffff;
          text-decoration:none;
          font-size:15px;
          font-weight:bold;
          box-shadow:
            0 10px 24px rgba(49,91,65,0.22),
            inset 0 1px 0 rgba(255,255,255,0.3);
        ">
          Redefinir minha senha
        </a>

      </div>

      <!-- Aviso -->
      <div style="
        padding:16px 18px;
        border-radius:14px;
        background:#f5faf6;
        border:1px solid #dbe7dd;
      ">

        <p style="
          margin:0;
          color:#718078;
          font-size:13px;
          line-height:1.5;
          text-align:center;
        ">
          Este link é válido por <strong style="color:#315b41;">
          10 minutos
          </strong> e poderá ser utilizado apenas uma vez.
        </p>

      </div>

      <p style="
        margin:24px 0 0;
        color:#9aa69f;
        font-size:12px;
        line-height:1.5;
        text-align:center;
      ">
        Se você não solicitou a recuperação de senha,
        pode ignorar este e-mail com segurança.
      </p>

    </div>

    <!-- Rodapé -->
    <div style="
      padding:18px;
      background:#f5faf6;
      border-top:1px solid #dbe7dd;
      text-align:center;
    ">
      <span style="
        color:#718078;
        font-size:11px;
      ">
        © MacroFlow · Seu progresso em fluxo.
      </span>
    </div>

  </div>

</div>
""";

        String body = """
        {
          "from": "MacroFlow <noreply@macroflow.app.br>",
          "to": ["%s"],
          "subject": "Clique no link para recuperar sua senha",
          "html": "%s"
        }
        """.formatted(
                destino,
                html.replace("\"", "\\\"").replace("\n", "")
        );
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(
                "https://api.resend.com/emails",
                request,
                String.class
        );
    }

    @Transactional
    public void validarCodigo(Usuario usuario, String codigo) throws Exception {

            CodigoEmail email = emailRepository.findTopByUsuarioIdOrderByDataCriacaoDesc(usuario.getId()).orElseThrow(
                    () -> new RuntimeException("Usuario não encontrado")
            );

            if(email.getDataExpiracao().isBefore(LocalDateTime.now())){
            throw  new Exception("Codigo inválido");

        }
            if(!codigo.equals(email.getCodigo())){
                throw new Exception("Codigo errado");

            }
            if(email.isUsado()){
            throw new Exception("Codigo já utilizado");
            }
        usuario.setAtivo(true);
        email.setUsado(true);

        usuarioRepository.save(usuario);
        emailRepository.save(email);


    }
    @Transactional
    public void alterarSenha(String codigo, String senha) throws Exception {
        TokenRecuperacao tokenRecuperacao =
                tokenRecuperacaoRepository.findByCodigo(codigo)
                        .orElseThrow(() ->
                                new Exception("Link de recuperação inválido"));

        if(tokenRecuperacao.getDataExpiracao().isBefore(LocalDateTime.now())){
            throw  new Exception("Peça outro link");

        }

        if(tokenRecuperacao.isUsado()){
            throw new Exception("Peça outro link");
        }
        Usuario usuario = tokenRecuperacao.getUsuario();

        ValidarSenha.validarSenha(senha);

        usuario.setSenha(passwordEncoder.encode(senha));
        tokenRecuperacao.setUsado(true);
        usuarioRepository.save(usuario);
        tokenRecuperacaoRepository.save(tokenRecuperacao);

    }
    public CodigoEmail buscarUltimo(Long usuarioId){
        return emailRepository.findTopByUsuarioIdOrderByDataCriacaoDesc(usuarioId).orElse(null);
    }
}
