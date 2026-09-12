package com.tcc.macroflow.ia.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MacroflowIaConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory){

        return  builder .defaultAdvisors(
                MessageChatMemoryAdvisor.builder(chatMemory).build()
        ).build();
    }

    @Bean
    public ChatMemory chatMemory (){
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
    }
}
;