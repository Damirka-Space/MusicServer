package com.dam1rka.musicserver.config;

import com.dam1rka.musicserver.security.oauth2.CustomOAuth2UserService;
import com.dam1rka.musicserver.security.oauth2.CustomOidcUserService;
import com.dam1rka.musicserver.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomOidcUserService oidcUserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    SecurityConfig(CustomOAuth2UserService oAuth2UserService, CustomOidcUserService oidcUserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler) {
        this.oAuth2UserService = oAuth2UserService;
        this.oidcUserService = oidcUserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
//                                .antMatchers("/**").permitAll()
                                .anyRequest().permitAll()
//                                .anyRequest().authenticated()
                );
//                .oauth2Login().loginPage("/oauth2/authorization/auth-server")
//                    .userInfoEndpoint().userService(oAuth2UserService)
//                    .oidcUserService(oidcUserService)
//                .and().successHandler(oAuth2AuthenticationSuccessHandler);
        http.csrf().disable();
        http.cors().disable();
        return http.build();
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1073741824); // 1 gb
        return multipartResolver;
    }
}
