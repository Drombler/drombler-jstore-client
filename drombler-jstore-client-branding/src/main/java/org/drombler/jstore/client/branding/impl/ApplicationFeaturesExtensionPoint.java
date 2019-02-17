package org.drombler.jstore.client.branding.impl;

import org.drombler.acp.core.application.ExtensionPoint;
import org.drombler.jstore.client.branding.jaxb.ApplicationFeaturesType;
import org.osgi.service.component.annotations.Component;


@Component
public class ApplicationFeaturesExtensionPoint implements ExtensionPoint<ApplicationFeaturesType> {

    @Override
    public Class<ApplicationFeaturesType> getJAXBRootClass() {
        return ApplicationFeaturesType.class;
    }

}
