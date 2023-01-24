package com.dam1rka.musicserver.controllers;

import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@RestController
public class Oauth2Controller {

    @Autowired
    private UserRepository userRepository;

    @Value("${website}")
    private String website;

    @GetMapping(value = "/login")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect(website);
    }

    @GetMapping(value = "/user")
    public UserEntity login(Principal principal, HttpServletResponse response) {
        if(Objects.nonNull(principal)) {
            response.addHeader("Access-Control-Allow-Credentials", "true");
            UserEntity user = userRepository.findByUsername(principal.getName());
            if(Objects.nonNull(user))
                return user;
        }
        return null;
    }

    @GetMapping(value = "/authorized", params = OAuth2ParameterNames.ERROR)
    public Map<String, OAuth2Error> authorizationFailed(HttpServletRequest request) {
        String errorCode = request.getParameter(OAuth2ParameterNames.ERROR);
        if (StringUtils.hasText(errorCode)) {
            Map<String, OAuth2Error> v = Collections.singletonMap("error",
                    new OAuth2Error(
                            errorCode,
                            request.getParameter(OAuth2ParameterNames.ERROR_DESCRIPTION),
                            request.getParameter(OAuth2ParameterNames.ERROR_URI))
            );
            return v;
        }

        return null;
    }

}
