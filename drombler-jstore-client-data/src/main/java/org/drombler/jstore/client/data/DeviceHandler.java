package org.drombler.jstore.client.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.data.AbstractDataHandler;
import org.drombler.acp.core.data.BusinessObjectHandler;
import org.drombler.jstore.client.integration.client.agent.JStoreClientAgentSocketClient;
import org.drombler.jstore.client.integration.store.StoreRestClient;
import org.drombler.jstore.client.integration.store.StoreRestClientRegistry;
import org.drombler.jstore.client.model.json.DeviceConfiguration;
import org.drombler.jstore.protocol.json.Store;
import org.drombler.jstore.protocol.json.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@BusinessObjectHandler
public class DeviceHandler extends AbstractDataHandler<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceHandler.class);

    private final DeviceConfiguration deviceConfiguration;
    private final ObjectMapper objectMapper;
    private final StoreRestClientRegistry storeRestClientRegistry;
    private final BooleanProperty modified = new SimpleBooleanProperty(this, "modified", false);
    private final ConnectedProperty connected = new ConnectedProperty();
    private JStoreClientAgentSocketClient jStoreClientAgentSocketClient;
    private Map<String, Store> stores = new HashMap<>();

    public DeviceHandler(DeviceConfiguration deviceConfiguration, ObjectMapper objectMapper, StoreRestClientRegistry storeRestClientRegistry) {
        this.deviceConfiguration = deviceConfiguration;
        this.objectMapper = objectMapper;
        this.storeRestClientRegistry = storeRestClientRegistry;
        if (StringUtils.isBlank(deviceConfiguration.getId())) {
            deviceConfiguration.setId(UUID.randomUUID().toString());
            setModified(true);
        }
        connected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                jStoreClientAgentSocketClient.getStores().stream()
                        .filter(store -> !stores.containsKey(store.getEndpoint()))
                        .forEach(store -> {
                            stores.put(store.getEndpoint(), store);
                            this.storeRestClientRegistry.registerStore(store, getUniqueKey());
                        });
            }
        });

    }

    public void connect() {
        int port = deviceConfiguration.getPort();
        boolean retry = true;
        while (retry) {
            try {
                jStoreClientAgentSocketClient = new JStoreClientAgentSocketClient(deviceConfiguration.getHost(), port, objectMapper);
                retry = false;
                setConnected(true);
                LOGGER.info("Connected to: {}", deviceConfiguration);
                if (port != deviceConfiguration.getPort()) {
                    deviceConfiguration.setPort(port);
                    setModified(true);
                }
            } catch (IllegalArgumentException ex) {
                setConnected(false);
                retry = false;
            } catch (IOException ex) {
                if (deviceConfiguration.getHost() == null) {
                    port++;
                } else {
                    setConnected(false);
                    retry = false;
                }
            }
        }
    }

    public List<StoreRestClient> getStoreRestClients() {
        return stores.values().stream()
                .map(storeRestClientRegistry::getStoreRestClient)
                .collect(Collectors.toList());
    }

    @Override
    public void close() {
        try {
            jStoreClientAgentSocketClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stores.forEach((s, store) -> storeRestClientRegistry.unregisterStore(store, getUniqueKey()));
        super.close();
    }

    @Override
    public String getTitle() {
        return deviceConfiguration.getDisplayName();
    }

    @Override
    public String getTooltipText() {
        return deviceConfiguration.toString();
    }

    @Override
    public String getUniqueKey() {
        return deviceConfiguration.getId();
    }

    public final boolean isModified() {
        return modifiedProperty().get();
    }

    public final void setModified(boolean modified) {
        modifiedProperty().set(modified);
    }

    public BooleanProperty modifiedProperty() {
        return modified;
    }

    public final boolean isConnected() {
        return connectedProperty().get();
    }

    private final void setConnected(boolean connected) {
        this.connected.set(connected);
    }

    public ReadOnlyBooleanProperty connectedProperty() {
        return connected;
    }

    public DeviceConfiguration getDeviceConfiguration() {
        return deviceConfiguration;
    }

    public boolean isMyComputer() {
        return StringUtils.isBlank(deviceConfiguration.getHost());
    }

    public JStoreClientAgentSocketClient getJStoreClientAgentSocketClient() {
        return jStoreClientAgentSocketClient;
    }

    private class ConnectedProperty extends ReadOnlyBooleanPropertyBase {

        private boolean connected = false;

        @Override
        public Object getBean() {
            return DeviceHandler.this;
        }

        @Override
        public String getName() {
            return "connected";
        }

        @Override
        public boolean get() {
            return connected;
        }

        private void set(boolean newValue) {
            if (connected != newValue) {
                connected = newValue;
                fireValueChangedEvent();
            }
        }
    }
}
