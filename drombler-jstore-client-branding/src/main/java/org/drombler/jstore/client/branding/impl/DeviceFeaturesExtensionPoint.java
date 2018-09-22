package org.drombler.jstore.client.branding.impl;

import org.drombler.acp.core.application.ExtensionPoint;
import org.drombler.jstore.client.branding.jaxb.DeviceFeaturesType;
import org.osgi.service.component.annotations.Component;


@Component
public class DeviceFeaturesExtensionPoint implements ExtensionPoint<DeviceFeaturesType> {

    @Override
    public Class<DeviceFeaturesType> getJAXBRootClass() {
        return DeviceFeaturesType.class;
    }

}
