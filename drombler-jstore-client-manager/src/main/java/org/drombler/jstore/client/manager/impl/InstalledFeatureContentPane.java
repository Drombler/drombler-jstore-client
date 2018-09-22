package org.drombler.jstore.client.manager.impl;

import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import org.drombler.commons.context.ActiveContextSensitive;
import org.drombler.commons.context.Context;
import org.drombler.commons.context.ContextEvent;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.jstore.client.branding.DeviceFeature;
import org.drombler.jstore.client.data.DeviceHandler;

import java.util.Objects;

@DeviceFeature(displayName = "%displayName", position = 10)
public class InstalledFeatureContentPane extends BorderPane implements ActiveContextSensitive {
    private Context activeContext;
    private DeviceHandler device;

    private ListView<Object> listView;

    public InstalledFeatureContentPane() {
        FXMLLoaders.loadRoot(this);
    }

    @Override
    public void setActiveContext(Context activeContext) {
        this.activeContext = activeContext;
        this.activeContext.addContextListener(DeviceHandler.class, this::contextChanged);
        contextChanged(new ContextEvent<>(activeContext, DeviceHandler.class));
    }

    private void contextChanged(ContextEvent<DeviceHandler> event) {
        DeviceHandler newDevice = activeContext.find(event.getType());
        if (!Objects.equals(device, newDevice)) {
            if (device != null) {
                unregister();
            }
            device = newDevice;
            if (device != null) {
                register();
            }
        }

    }

    private void register() {

    }

    private void unregister() {
    }
}
