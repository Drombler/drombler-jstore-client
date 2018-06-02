package org.drombler.jstore.client.integration.store;

public interface StoreRestClientProvider {
    StoreRestClient getStoreRestClient(String storeId);
}
