package com.dam1rka.musicserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers("/main/**", "/album/**", "/channel/**").permitAll()
                            .anyRequest().hasAuthority("SCOPE_music-server.read");
                }).oauth2ResourceServer(resourceServerConfigurer -> resourceServerConfigurer.opaqueToken(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }
}
