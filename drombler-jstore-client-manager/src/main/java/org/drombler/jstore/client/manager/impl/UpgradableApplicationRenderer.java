package org.drombler.jstore.client.manager.impl;

import org.drombler.commons.fx.scene.renderer.AbstractDataRenderer;
import org.drombler.jstore.protocol.json.UpgradableApplication;

/**
 *
 * @author puce
 */
public class UpgradableApplicationRenderer extends AbstractDataRenderer<UpgradableApplication> {

    @Override
    public String getText(UpgradableApplication item) {
        return item.getApplicationId().getGroupId() +":"+ item.getApplicationId().getArtifactId() + " v" + item.getLatestUpgradableApplicationVersion();
    }

}
