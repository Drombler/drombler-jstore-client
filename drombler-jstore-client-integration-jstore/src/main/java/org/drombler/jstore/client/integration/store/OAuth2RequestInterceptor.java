package org.drombler.jstore.client.integration.store;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.keycloak.adapters.ServerRequest;
import org.keycloak.adapters.installed.KeycloakInstalled;
import org.keycloak.common.VerificationException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OAuth2RequestInterceptor implements RequestInterceptor {
    private final KeycloakInstalled keycloakInstalled;

    public OAuth2RequestInterceptor(KeycloakInstalled keycloakInstalled) {
        this.keycloakInstalled = keycloakInstalled;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (keycloakInstalled.getToken() != null) {
            try {
                String tokenString = keycloakInstalled.getTokenString(2, TimeUnit.MINUTES);
                requestTemplate.header("Authorization", "Bearer " + tokenString);
            } catch (VerificationException | IOException | ServerRequest.HttpFailure e) {
                e.printStackTrace();
            }
        }
    }
}
