package org.drombler.jstore.client.store.impl.converter;

import org.drombler.jstore.protocol.json.KeycloakClientConfig;
import org.keycloak.representations.adapters.config.AdapterConfig;

public class AdapterConfigConverter {
    public AdapterConfig convertAdapterConfig(KeycloakClientConfig keycloakClientConfig) {
        AdapterConfig adapterConfig = new AdapterConfig();
        adapterConfig.setAuthServerUrl(keycloakClientConfig.getAuthServerUrl());
        adapterConfig.setRealm(keycloakClientConfig.getRealm());
        adapterConfig.setResource(keycloakClientConfig.getResource());
        adapterConfig.setSslRequired(keycloakClientConfig.getSslRequired().value());
        adapterConfig.setPublicClient(true);
        adapterConfig.setUseResourceRoleMappings(true);
        return adapterConfig;
    }
}
