package org.drombler.jstore.client.branding.impl;

import org.drombler.commons.fx.scene.renderer.AbstractDataRenderer;
import org.drombler.jstore.client.branding.DeviceFeatureDescriptor;

public class DeviceFeatureRenderer extends AbstractDataRenderer<DeviceFeatureDescriptor<?>> {

    @Override
    public String getText(DeviceFeatureDescriptor<?> item) {
        if (item != null) {
            return item.getDisplayName();
        } else {
            return null;
        }
    }

}
