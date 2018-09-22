package org.drombler.jstore.client.branding.impl;

import javafx.scene.Node;
import org.drombler.commons.fx.scene.control.DataToggleButton;
import org.drombler.jstore.client.branding.DeviceFeatureDescriptor;

public class DeviceFeatureToggleButton extends DataToggleButton<DeviceFeatureDescriptor<? extends Node>> {

    public DeviceFeatureToggleButton(DeviceFeatureDescriptor<? extends Node> deviceFeatureDescriptor) {
        super(new DeviceFeatureRenderer());
        setData(deviceFeatureDescriptor);
    }

}
