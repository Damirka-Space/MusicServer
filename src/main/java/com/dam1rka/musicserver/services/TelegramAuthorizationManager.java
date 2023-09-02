package com.dam1rka.musicserver.services;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class TelegramAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    // TODO: make token checking
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {

        int test = 0;

        return new AuthorizationDecision(true);
    }
}
