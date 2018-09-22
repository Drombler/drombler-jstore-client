package org.drombler.jstore.client.model.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.drombler.jstore.client.model.ObjectMapperProvider;
import org.osgi.service.component.annotations.Component;

@Component
public class ObjectMapperProviderImpl implements ObjectMapperProvider {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
