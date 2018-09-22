package org.drombler.jstore.client.manager.impl;

import org.drombler.fx.startup.main.DromblerFXConfiguration;
import org.drombler.jstore.client.manager.StoreManager;
import org.drombler.jstore.protocol.StoreRegistry;
import org.drombler.jstore.protocol.json.Store;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import java.nio.file.Path;
import java.util.List;

@Component
public class StoreManagerImpl implements StoreManager {
    @Reference
    private DromblerFXConfiguration dromblerFXConfiguration;

    private final StoreRegistry storeRegistry = new StoreRegistry();

    @Activate
    protected void activate(ComponentContext context) {
        Path storesJsonFilePath = dromblerFXConfiguration.getUserConfigDirPath().resolve("stores.json");

    }


    @Deactivate
    protected void deactivate(ComponentContext context) {

    }

    @Override
    public List<Store> getConfiguredStores() {
        return null;
    }

    @Override
    public void addStore(Store store) {
        storeRegistry.registerStore(store);
        updateStoresConfigFile();
    }

    @Override
    public void removeStore(String storeId) {
        storeRegistry.unregisterStore(storeId);
        updateStoresConfigFile();
    }

    private void updateStoresConfigFile() {

    }
}
