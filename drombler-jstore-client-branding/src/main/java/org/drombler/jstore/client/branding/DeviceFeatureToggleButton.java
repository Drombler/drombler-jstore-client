package org.drombler.jstore.client.branding;

import javafx.scene.Node;
import org.drombler.commons.fx.scene.control.DataToggleButton;

public class DeviceFeatureToggleButton extends DataToggleButton<DeviceFeatureDescriptor<? extends Node>> {

    public DeviceFeatureToggleButton(DeviceFeatureDescriptor<? extends Node> deviceFeatureDescriptor) {
        super(new DeviceFeatureRenderer());
        setData(deviceFeatureDescriptor);
    }

}
