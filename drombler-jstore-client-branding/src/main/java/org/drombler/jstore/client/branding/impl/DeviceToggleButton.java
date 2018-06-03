package org.drombler.jstore.client.branding.impl;

import org.drombler.commons.context.Context;
import org.drombler.commons.context.LocalContextProvider;
import org.drombler.commons.context.SimpleContext;
import org.drombler.commons.context.SimpleContextContent;
import org.drombler.commons.fx.scene.control.DataToggleButton;
import org.drombler.jstore.client.data.DeviceHandler;

public class DeviceToggleButton extends DataToggleButton<DeviceHandler> implements LocalContextProvider {
    private final SimpleContextContent contextContent = new SimpleContextContent();
    private final Context context = new SimpleContext(contextContent);

    public DeviceToggleButton(DeviceHandler device) {
        super(new DeviceRenderer());
        dataProperty().addListener((observable, oldValue, newValue) -> manageContextContent(oldValue, newValue));
        setData(device);
    }

    private void manageContextContent(DeviceHandler oldValue, DeviceHandler newValue) {
        if (oldValue != null) {
            contextContent.remove(oldValue);
        }
        if (newValue != null) {
            contextContent.add(newValue);
        }
    }

    @Override
    public Context getLocalContext() {
        return context;
    }
}
