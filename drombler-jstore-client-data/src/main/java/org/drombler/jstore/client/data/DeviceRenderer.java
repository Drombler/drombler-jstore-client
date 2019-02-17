package org.drombler.jstore.client.data;

import javafx.scene.control.Tooltip;
import org.drombler.commons.fx.scene.renderer.AbstractDataRenderer;
import org.drombler.jstore.client.data.DeviceHandler;

public class DeviceRenderer extends AbstractDataRenderer<DeviceHandler> {
    private final Tooltip tooltip = new Tooltip();

    @Override
    public String getText(DeviceHandler item) {
        if (item != null) {
            return item.getTitle();
        } else {
            return null;
        }
    }

    @Override
    public Tooltip getTooltip(DeviceHandler item) {
        if (item != null) {
            tooltip.setText(item.getTooltipText());
            return tooltip;
        } else {
            tooltip.setText(null);
            return null;
        }
    }
}
