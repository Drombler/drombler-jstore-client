package org.drombler.jstore.client.branding;

import org.drombler.commons.fx.scene.renderer.AbstractDataRenderer;

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
