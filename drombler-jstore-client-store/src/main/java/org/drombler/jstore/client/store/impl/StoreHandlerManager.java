package org.drombler.jstore.client.store.impl;

import java.util.HashSet;
import java.util.Set;
import org.drombler.jstore.client.store.StoreHandler;

/**
 *
 * @author puce
 */
public class StoreHandlerManager {

    private final StoreHandler storeHandler;
    private final Set<String> deviceIds = new HashSet<>();
    private boolean usedDeviceIndependently = false;


    public StoreHandlerManager(StoreHandler storeHandler) {
        this.storeHandler = storeHandler;

    }

    public void addDeviceId(String deviceId) {
        deviceIds.add(deviceId);
    }

    public void removeDeviceId(String deviceId) {
        deviceIds.remove(deviceId);
    }

    public boolean containsDeviceId(String deviceId) {
        return deviceIds.contains(deviceId);
    }

    /**
     * @return the storeHandler
     */
    public StoreHandler getStoreHandler() {
        return storeHandler;
    }

    public void setUsedDeviceIndependently() {
        this.usedDeviceIndependently = true;
    }

    public boolean isInUse() {
        return usedDeviceIndependently || !deviceIds.isEmpty();
    }
}
