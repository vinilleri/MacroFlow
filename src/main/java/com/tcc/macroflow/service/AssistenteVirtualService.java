package com.tcc.macroflow.service;

import com.tcc.macroflow.dto.AssistenteDTO;
import com.tcc.macroflow.dto.MacroDTO;

import com.tcc.macroflow.dto.MetaResponseDTO;
import com.tcc.macroflow.dto.ObjetivoResponseDTO;
import com.tcc.macroflow.model.AssistenteVirtual;
import com.tcc.macroflow.model.Usuario;
import com.tcc.macroflow.repository.AssistenteVirtualRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AssistenteVirtualService {
    private final AssistenteVirtualRepository assistenteVirtualRepository;
    private final AuthService authService;
    private final ConsumoService consumoService;
    private final MetaService metaService;
    private final ObjetivoService objetivoService;
    @Value("${groq.api.key}")
    private String apiKey;


    public AssistenteVirtualService(AssistenteVirtualRepository assistenteVirtualRepository, AuthService authService,
                                    ConsumoService consumoService, MetaService metaService, ObjetivoService objetivoService) {
        this.assistenteVirtualRepository = assistenteVirtualRepository;
        this.authService = authService;
        this.consumoService = consumoService;
        this.metaService = metaService;
        this.objetivoService = objetivoService;
    }
    private AssistenteDTO transformarDto(AssistenteVirtual virtual){
        return new AssistenteDTO(virtual.getMensagemUsuario(), virtual.getMensagemIa() );
    }

    private StringBuilder historicoConversa(Usuario usuario){
        List<AssistenteVirtual> historico= assistenteVirtualRepository.findTop5ByUsuarioIdOrderByDataHoraDesc(usuario.getId());

        StringBuilder promptHistorico = new StringBuilder();
        for(AssistenteVirtual mensagem: historico.reversed()) {
            promptHistorico.append("Usuario: ")
                    .append(mensagem.getMensagemUsuario())
                    .append("\n");

            promptHistorico.append("Assistente: ")
                    .append(mensagem.getMensagemIa())
                    .append("\n\n");
        }
        return promptHistorico;
    }

    private String montarPrompt(Usuario usuario,String pergunta){

        MacroDTO consumoAtual = consumoService.somarConsumoDia();
        MetaResponseDTO metaAtual = metaService.buscarMetaAtual();
        StringBuilder historico = historicoConversa(usuario);
        ObjetivoResponseDTO objetivoAtual = objetivoService.objetivoAtual();

        return """
                Você é um assistente virtual de um site de acompanhamento nutricional. Responda as perguntas de forma curta e amigável.
                Se o usuário fizer perguntas que não sejam relacionadas a nutrição,
                alimentação, hábitos saudáveis ou ao sistema, responda educadamente
                que seu foco é auxiliar no acompanhamento nutricional.
                
                    Dados do usuário:
                        Nome: %s
                
                        Consumo atual:
                        Calorias: %.2f
                        Proteínas: %.2f g
                        Carboidratos: %.2f g
                        Gorduras: %.2f g
                
                        Meta diária:
                        Calorias: %.2f
                        Proteínas: %.2f g
                        Carboidratos: %.2f g
                        Gorduras: %.2f g
                        
                        Objetivo atual: 
                        %s
                        
                        Historico recente:
                        %s
                        
                        Pergunta do usuário:
                        %s
                
                        
                        Dê orientações gerais e motivacionais baseadas nesses dados.
                        Não prescreva dietas médicas.
                """
                .formatted(
                        usuario.getNome(),
                        consumoAtual.getCalorias(),
                        consumoAtual.getProteinas(),
                        consumoAtual.getCarboidrato(),
                        consumoAtual.getGordura(),
                        metaAtual.getCalorias(),
                        metaAtual.getProteinas(),
                        metaAtual.getCarboidrato(),
                        metaAtual.getGordura(),
                        objetivoAtual.getTipoObjetivo(),
                        historico,
                        pergunta
                );

    }
    private String respostaIa(String pergunta) {
        RestClient restClient = RestClient.create();
        Usuario usuario = authService.getUsuario();
        String prompt = montarPrompt(usuario,pergunta);

        Map<String, Object> body = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                )
        );

        String resposta = restClient.post()
                .uri("https://api.groq.com/openai/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(resposta);

            return jsonNode
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asString();
        }
        catch (Exception e){
            throw new RuntimeException("Erro ao processar resposta da IA", e);
        }
    }

    @Transactional
    public String enviarMensagem(String pergunta){
        Usuario usuario = authService.getUsuario();
        AssistenteVirtual assistenteVirtual = new AssistenteVirtual();
        assistenteVirtual.setUsuario(usuario);
        assistenteVirtual.setMensagemUsuario(pergunta);
        String resposta = respostaIa(pergunta);
        assistenteVirtual.setMensagemIa(resposta);
        assistenteVirtual.setDataHora(LocalDateTime.now());

        assistenteVirtualRepository.save(assistenteVirtual);

        return assistenteVirtual.getMensagemIa();
    }

public List<AssistenteDTO> listarMensagens() {
    Usuario usuario = authService.getUsuario();

    List<AssistenteVirtual> lista = assistenteVirtualRepository.findAllByUsuarioIdOrderByDataHoraAsc(usuario.getId());

    return lista.stream()
            .map(this::transformarDto)
            .toList();
}

@Transactional
public void deletarConversa(){
        Usuario usuario = authService.getUsuario();
        assistenteVirtualRepository.deleteByUsuarioId(usuario.getId());
}




}
