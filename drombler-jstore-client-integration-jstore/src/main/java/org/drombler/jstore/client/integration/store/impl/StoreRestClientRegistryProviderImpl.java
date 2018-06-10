package org.drombler.jstore.client.integration.store.impl;

import org.drombler.jstore.client.integration.store.StoreRestClientRegistry;
import org.drombler.jstore.client.integration.store.StoreRestClientRegistryProvider;
import org.osgi.service.component.annotations.Component;


@Component
public class StoreRestClientRegistryProviderImpl implements StoreRestClientRegistryProvider {

    private final StoreRestClientRegistry storeRestClientRegistry = new StoreRestClientRegistry();


    @Override
    public StoreRestClientRegistry getStoreRestClientRegistry() {
        return storeRestClientRegistry;
    }
}
