package org.drombler.jstore.client.integration.jstore.impl;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.drombler.fx.startup.main.DromblerFXConfiguration;
import org.drombler.jstore.client.integration.jstore.JStoreRestClient;
import org.drombler.jstore.client.integration.jstore.JStoreRestClientProvider;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;


@Component
public class JStoreRestClientProviderImpl implements JStoreRestClientProvider {
    public static final String JSTORE_ENDPOINT_PROPERTY = "jstore.endpoint";

    @Reference
    private DromblerFXConfiguration dromblerFXConfiguration;

    private JStoreRestClient jStoreRestClient;

    @Activate
    protected void activate(ComponentContext context) {
        jStoreRestClient = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger())
                .client(new OkHttpClient())
                .target(JStoreRestClient.class, getStoreEndpoint());

    }

    private String getStoreEndpoint() {
        return dromblerFXConfiguration.getUserConfigProps().getProperty(JSTORE_ENDPOINT_PROPERTY);
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        jStoreRestClient = null;
    }

    @Override
    public JStoreRestClient getJStoreRestClient() {
        return jStoreRestClient;
    }
}
