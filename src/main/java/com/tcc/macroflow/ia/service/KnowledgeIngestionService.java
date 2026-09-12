package com.tcc.macroflow.ia.service;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class KnowledgeIngestionService {
    private  final VectorStore vectorStore;
    private final TokenTextSplitter tokenTextSplitter;
    public KnowledgeIngestionService(VectorStore vectorStore, TokenTextSplitter tokenTextSplitter) {
        this.vectorStore = vectorStore;
        this.tokenTextSplitter = tokenTextSplitter;
    }

    public void carregarConhecimento()  {

        vectorStore.delete("tipo == 'conhecimento'");

        PathMatchingResourcePatternResolver resolver =
                new PathMatchingResourcePatternResolver();
        try {
        Resource[] resources = resolver.getResources("classpath:/knowledge/*.md");

        for(Resource resource: resources) {
            String conteudo = resource.getContentAsString(StandardCharsets.UTF_8);

            Document document = new Document(conteudo,
                    Map.of(
                            "fonte", Objects.requireNonNull(resource.getFilename()).replace("md",""),
                            "tipo", "conhecimento"
                    ));
            List<Document> splitDocs = tokenTextSplitter.apply(List.of(document));

            vectorStore.add(splitDocs);
        }
        }
        catch (IOException e){
            throw new RuntimeException(
                    "Erro ao carregar conhecimento do MacroFlow", e
            );
        }
    }
}
