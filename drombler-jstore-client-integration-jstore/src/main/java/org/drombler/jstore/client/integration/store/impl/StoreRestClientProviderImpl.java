package org.drombler.jstore.client.integration.store.impl;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.drombler.fx.startup.main.DromblerFXConfiguration;
import org.drombler.jstore.client.integration.store.StoreRestClient;
import org.drombler.jstore.client.integration.store.StoreRestClientProvider;
import org.drombler.jstore.client.protocol.json.Store;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;


@Component
public class StoreRestClientProviderImpl implements StoreRestClientProvider {
    public static final String JSTORE_ENDPOINT_PROPERTY = "jstore.endpoint";

    @Reference
    private DromblerFXConfiguration dromblerFXConfiguration;

    private StoreRestClient storeRestClient;

    @Activate
    protected void activate(ComponentContext context) {
        Store store = null;
        storeRestClient = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger())
//                .client(new OkHttpClient())
                .target(StoreRestClient.class, store.getEndpoint());

    }


    @Deactivate
    protected void deactivate(ComponentContext context) {
        storeRestClient = null;
    }

    @Override
    public StoreRestClient getStoreRestClient(String storeId) {
        return storeRestClient;
    }
}
