package org.drombler.jstore.client.store.impl;

import java.io.IOException;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.drombler.jstore.client.store.StoreHandlerRegistry;
import org.osgi.service.component.annotations.Component;
import org.drombler.jstore.client.store.StoreHandlerRegistryProvider;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component
public class StoreRestClientRegistryProviderImpl implements StoreHandlerRegistryProvider {

    @Reference
    private DataHandlerRegistryProvider dataHandlerRegistryProvider;

    private StoreHandlerRegistry storeRestClientRegistry;

    @Activate
    protected void activate(ComponentContext context) throws IOException {
        this.storeRestClientRegistry = new StoreHandlerRegistry(dataHandlerRegistryProvider.getDataHandlerRegistry());
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        this.storeRestClientRegistry = null;
    }

    @Override
    public StoreHandlerRegistry getStoreHandlerRegistry() {
        return storeRestClientRegistry;
    }
}
