package org.drombler.jstore.client.manager;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface ObjectMapperProvider {
    ObjectMapper getObjectMapper();
}
