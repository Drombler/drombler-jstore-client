package org.drombler.jstore.client.manager.impl;

import org.drombler.commons.fx.scene.renderer.AbstractDataRenderer;
import org.drombler.jstore.protocol.json.UpgradableApplication;
import org.drombler.jstore.protocol.json.UpgradableJRE;

/**
 *
 * @author puce
 */
public class UpgradableJreRenderer extends AbstractDataRenderer<UpgradableJRE> {

    @Override
    public String getText(UpgradableJRE item) {
        return item.getJreInfo().getJavaSpecificationVersion() +" ("+ item.getJreInfo().getJreVendorId() + "): v" + item.getLatestUpgradableJREImplementationVersion();
    }

}
