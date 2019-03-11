package org.drombler.jstore.client.store;

import org.drombler.jstore.protocol.json.Store;

import java.util.List;

public interface StoreManager {
    List<Store> getConfiguredStores();

    void addStore(Store store);

    void removeStore(String storeId);
}
