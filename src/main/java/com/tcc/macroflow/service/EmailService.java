package com.tcc.macroflow.service;

import com.tcc.macroflow.model.CodigoEmail;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.EmailRepository;
import com.tcc.macroflow.repository.UsuarioRepository;
import jakarta.mail.internet.MimeMessage;

import jakarta.transaction.Transactional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class EmailService
{

    private final EmailRepository emailRepository;
    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;
    private static final SecureRandom random = new SecureRandom();


    public EmailService(EmailRepository emailRepository, UsuarioRepository usuarioRepository, JavaMailSender mailSender) {
        this.emailRepository = emailRepository;
        this.usuarioRepository = usuarioRepository;

        this.mailSender = mailSender;
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
        MimeMessage mensagem = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
        helper.setFrom("onboarding@resend.dev");
        helper.setTo(destino);
        helper.setSubject("Código de Verificação");

        String html = """
        <div style="font-family: Arial; text-align: center;">
            <h2>Verificação de Conta</h2>
            <p>Use o código abaixo para verificar seu email:</p> 
            <div style="
                font-size: 24px;
                font-weight: bold;
                background-color: #f2f2f2;
                padding: 10px;
                display: inline-block;
                border-radius: 8px;
            ">
                """ + codigoEmail.getCodigo() + """
            </div>

            <p style="margin-top: 20px;">Esse código expira em alguns minutos.</p>
        </div>
    """;

        helper.setText(html, true); // true = HTML

        mailSender.send(mensagem);
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
