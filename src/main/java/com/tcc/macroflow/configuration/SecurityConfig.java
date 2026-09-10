package com.tcc.macroflow.configuration;

import com.tcc.macroflow.component.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return   http
                .cors(cors -> {})
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/usuario").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/atividade-fisica").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/usuario/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/usuario/login/confirmar/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/usuario/recuperacao").permitAll()
                            .requestMatchers(HttpMethod.PATCH, "/api/usuario/recuperacao/alterarSenha").permitAll()
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .requestMatchers("/swagger-ui/**").permitAll()
                            .requestMatchers("/swagger-ui.html").permitAll()
                            .anyRequest().authenticated()
                    ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();


    }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }


