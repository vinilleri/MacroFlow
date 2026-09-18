package com.tcc.macroflow.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

    @Configuration
    public class CorsConfig {

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedOrigins(List.of(
                    "http://127.0.0.1:5500",
                    "http://localhost:5500",
                    "http://147.15.101.13",
                    "http://macroflow.app.br"

            ));

            configuration.setAllowedMethods(List.of(
                    "GET",
                    "POST",
                    "PUT",
                    "PATCH",
                    "DELETE",
                    "OPTIONS"
            ));

            configuration.setAllowedHeaders(List.of("*"));

            UrlBasedCorsConfigurationSource source =
                    new UrlBasedCorsConfigurationSource();

            source.registerCorsConfiguration("/**", configuration);

            return source;

        }
    }