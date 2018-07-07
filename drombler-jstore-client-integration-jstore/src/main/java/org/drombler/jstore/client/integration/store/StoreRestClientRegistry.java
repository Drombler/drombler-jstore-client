package org.drombler.jstore.client.integration.store;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.drombler.jstore.protocol.json.Store;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StoreRestClientRegistry {
    private final Map<String, StoreRestClient> storeRestClientMap = new HashMap<>();
    private final Map<String, Set<String>> deviceIdMap = new HashMap<>();

    private final Feign.Builder feignBuilder = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
//                .client(new OkHttpClient())
            .logger(new Slf4jLogger());


    public void registerStore(Store store, String deviceId) {
        if (containsStoreRegistration(store, deviceId)) {
            throw new IllegalArgumentException("Store with endpoint " + store.getEndpoint() + " already registered for device with device id" + deviceId + "!");
        }
        if (!storeRestClientMap.containsKey(store.getEndpoint())) {
            StoreRestClient storeRestClient = feignBuilder.target(StoreRestClient.class, store.getEndpoint());
            storeRestClientMap.put(store.getEndpoint(), storeRestClient);
            deviceIdMap.put(store.getEndpoint(), new HashSet<>());
        }
        deviceIdMap.get(store.getEndpoint()).add(deviceId);
    }

    public void unregisterStore(Store store, String deviceId) {
        if (!containsStoreRegistration(store, deviceId)) {
            throw new IllegalArgumentException("Store with endpoint " + store.getEndpoint() + " not registered for device with device id" + deviceId + "!");
        }
        deviceIdMap.get(store.getEndpoint()).remove(deviceId);
        if (deviceIdMap.get(store.getEndpoint()).isEmpty()) {
            storeRestClientMap.remove(store.getEndpoint());
            deviceIdMap.remove(store.getEndpoint());
        }
    }

    public StoreRestClient getStoreRestClient(Store store) {
        return storeRestClientMap.get(store.getEndpoint());
    }

    public boolean containsStoreRegistration(Store store, String deviceId) {
        return deviceIdMap.containsKey(store.getEndpoint()) && deviceIdMap.get(store.getEndpoint()).contains(deviceId);
    }
}
