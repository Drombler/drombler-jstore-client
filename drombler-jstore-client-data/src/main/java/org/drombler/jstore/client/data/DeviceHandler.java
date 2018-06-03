package org.drombler.jstore.client.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.data.AbstractDataHandler;
import org.drombler.acp.core.data.BusinessObjectHandler;
import org.drombler.jstore.client.integration.client.agent.JStoreClientAgentSocketClient;
import org.drombler.jstore.client.model.json.DeviceConfiguration;

import java.io.IOException;
import java.util.UUID;

@BusinessObjectHandler
public class DeviceHandler extends AbstractDataHandler<String> {
    private final DeviceConfiguration deviceConfiguration;
    private final ObjectMapper objectMapper;
    private final BooleanProperty modified = new SimpleBooleanProperty(this, "modified", false);
    private final BooleanProperty connected = new SimpleBooleanProperty(this, "connected", false);
    private JStoreClientAgentSocketClient jStoreClientAgentSocketClient;

    public DeviceHandler(DeviceConfiguration deviceConfiguration, ObjectMapper objectMapper) {
        this.deviceConfiguration = deviceConfiguration;
        this.objectMapper = objectMapper;
        if (StringUtils.isBlank(deviceConfiguration.getId())) {
            deviceConfiguration.setId(UUID.randomUUID().toString());
            setModified(true);
        }
        registerClientAgent();
    }

    private void registerClientAgent() {
        int port = deviceConfiguration.getPort();
        boolean retry = true;
        while (retry) {
            try {
                jStoreClientAgentSocketClient = new JStoreClientAgentSocketClient(deviceConfiguration.getHost(), port, objectMapper);
                retry = false;
                setConnected(true);
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

    public final void setConnected(boolean connected) {
        connectedProperty().set(connected);
    }

    public BooleanProperty connectedProperty() {
        return connected;
    }

    public DeviceConfiguration getDeviceConfiguration() {
        return deviceConfiguration;
    }

    public boolean isMyComputer() {
        return StringUtils.isBlank(deviceConfiguration.getHost());
    }
}
