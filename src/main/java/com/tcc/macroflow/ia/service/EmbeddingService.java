package com.tcc.macroflow.ia.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import java.util.List;
@Service

public class EmbeddingService {

    private final VectorStore vectorStore;

    public EmbeddingService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void adicionarConheciomento(){
        Document document = new Document(
                "O MacroFlow calcula a meta nutricional do usuário " +
                        "com base em suas medidas corporais, objetivo e nível de atividade física."
        );

        vectorStore.add(List.of(document));
    }

    public List<Document> buscar(String pergunta) {
        return vectorStore.similaritySearch(pergunta);
    }
}
