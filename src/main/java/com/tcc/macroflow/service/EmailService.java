package com.tcc.macroflow.service;

import com.tcc.macroflow.model.CodigoEmail;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.EmailRepository;
import com.tcc.macroflow.repository.UsuarioRepository;
import jakarta.mail.internet.MimeMessage;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class EmailService
{

    private final EmailRepository emailRepository;
    private final UsuarioRepository usuarioRepository;
    @Value("${resend.api.key}")
    private String apiKey;
    private static final SecureRandom random = new SecureRandom();


    public EmailService(EmailRepository emailRepository, UsuarioRepository usuarioRepository) {
        this.emailRepository = emailRepository;
        this.usuarioRepository = usuarioRepository;
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

    public void enviarCodigo(String destino, CodigoEmail codigoEmail) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String html = """
<div style="font-family: Arial, sans-serif; text-align:center; background-color:#f9f9f9; padding:40px 0;">

  <div style="background-color:#ffffff; max-width:400px; margin:0 auto; border-radius:8px; overflow:hidden; box-shadow:0 4px 8px rgba(0,0,0,0.1);">

    <div style="background-color:#71ffa7; height:15px;"></div>

    <div style="padding:30px;">

      <h2 style="color:#333333;">Verificação de Conta</h2>

      <p style="color:#666666; font-size:16px;">
        Use o código abaixo para verificar seu email:
      </p>

      <div style="
        font-size:32px;
        font-weight:bold;
        letter-spacing:4px;
        color:#333333;
        background-color:#f2f2f2;
        padding:20px 40px;
        margin:25px auto;
        display:inline-block;
        border-radius:8px;
        border:1px solid #e0e0e0;
      ">
        """ + codigoEmail.getCodigo() + """
      </div>

      <p style="color:#888888; font-size:14px;">
        Esse código expira em alguns minutos.
      </p>

    </div>
  </div>
</div>
""";
        String body = """
        {
          "from": "MacroFlow <noreply@macroflow.app.br>",
          "to": ["%s"],
          "subject": "Código de Verificação",
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
    public CodigoEmail buscarUltimo(Long usuarioId){
        return emailRepository.findTopByUsuarioIdOrderByDataCriacaoDesc(usuarioId).orElse(null);



    }
}
