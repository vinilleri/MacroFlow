package com.tcc.macroflow.ia.configuration;

import com.tcc.macroflow.ia.service.KnowledgeIngestionService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import java.util.Map;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
@Configuration
public class MacroflowIaConfig {

    @Bean
    public VectorStoreDocumentRetriever documentRetriever(VectorStore vectorStore) {
        return VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .build();
    }

    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStoreDocumentRetriever vectorStoreDocumentRetriever){
        return RetrievalAugmentationAdvisor.builder().
                documentRetriever(vectorStoreDocumentRetriever).build();

    }
    @Bean
    public TokenTextSplitter tokenTextSplitter(){
        return TokenTextSplitter.builder().build();
    }

    @Bean
    CommandLineRunner carregarConhecimento(KnowledgeIngestionService conhecimento){

        return args -> conhecimento.carregarConhecimento();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory,  RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {

        return builder
                .defaultOptions(
                        OpenAiChatOptions.builder()
                                .extraBody(Map.of("include_reasoning", false))
                )
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        retrievalAugmentationAdvisor
                )
                .build();
    }
    @Bean
    public ChatMemory chatMemory (){
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
    }
}
;