package com.dam1rka.musicserver.config;

import com.dam1rka.musicserver.services.TelegramAuthorizationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TelegramAuthorizationManager telegramAuthorizationManager;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers("/album/upload/").access(telegramAuthorizationManager)
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
