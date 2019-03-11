package org.drombler.jstore.client.store;

import java.util.HashMap;
import java.util.Map;
import org.drombler.commons.data.DataHandlerRegistry;
import org.drombler.jstore.client.store.impl.StoreHandlerManager;

public class StoreHandlerRegistry {

    private final Map<String, StoreHandlerManager> storeHandlerManagers = new HashMap<>();
    private final DataHandlerRegistry dataHandlerRegistry;

    public StoreHandlerRegistry(DataHandlerRegistry dataHandlerRegistry) {
        this.dataHandlerRegistry = dataHandlerRegistry;
    }

    public StoreHandler registerStoreHandler(StoreHandler storeHandler, String deviceId) {
        if (containsStoreRegistration(storeHandler, deviceId)) {
            throw new IllegalArgumentException("Store with endpoint " + storeHandler.getStore().getEndpoint() + " already registered for device with device id" + deviceId + "!");
        }
        StoreHandler registeredStoreHandler = registerStoreHandler(storeHandler);
        storeHandlerManagers.get(registeredStoreHandler.getUniqueKey()).addDeviceId(deviceId);
        return registeredStoreHandler;
    }

    public StoreHandler registerStoreHandler(StoreHandler storeHandler) {
        StoreHandler registeredStoreHandler = (StoreHandler) dataHandlerRegistry.registerDataHandler(storeHandler);
        if (!storeHandlerManagers.containsKey(registeredStoreHandler.getUniqueKey())) {
            StoreHandlerManager storeRestClientManager = new StoreHandlerManager(registeredStoreHandler);
            storeHandlerManagers.put(registeredStoreHandler.getUniqueKey(), storeRestClientManager);
            registeredStoreHandler.addCloseEventListener(evt -> unregisterStoreHandler(storeHandler));
        }
        return registeredStoreHandler;
    }

    public void unregisterStoreHandler(StoreHandler storeHandler, String deviceId) {
        if (!containsStoreRegistration(storeHandler, deviceId)) {
            throw new IllegalArgumentException("Store with endpoint " + storeHandler.getStore().getEndpoint() + " not registered for device with device id" + deviceId + "!");
        }
        storeHandlerManagers.get(storeHandler.getUniqueKey()).removeDeviceId(deviceId);
        if (!storeHandlerManagers.get(storeHandler.getUniqueKey()).isInUse()) {
            unregisterStoreHandler(storeHandler);
        }
    }

    private void unregisterStoreHandler(StoreHandler storeHandler) {
        dataHandlerRegistry.unregisterDataHandler(storeHandler);
        storeHandlerManagers.remove(storeHandler.getUniqueKey());
    }

    public boolean containsStoreRegistration(StoreHandler storeHandler, String deviceId) {
        return storeHandlerManagers.containsKey(storeHandler.getUniqueKey()) && storeHandlerManagers.get(storeHandler.getUniqueKey()).containsDeviceId(deviceId);
    }
}
