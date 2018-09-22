package org.drombler.jstore.client.model;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface ObjectMapperProvider {
    ObjectMapper getObjectMapper();
}
