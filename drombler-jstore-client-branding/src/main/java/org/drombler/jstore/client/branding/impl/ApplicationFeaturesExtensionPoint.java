package org.drombler.jstore.client.branding.impl;

import org.drombler.acp.core.application.ExtensionPoint;
import org.drombler.jstore.client.branding.jaxb.ApplicationFeatureType;
import org.osgi.service.component.annotations.Component;


@Component
public class ApplicationFeaturesExtensionPoint implements ExtensionPoint<ApplicationFeatureType> {

    @Override
    public Class<ApplicationFeatureType> getJAXBRootClass() {
        return ApplicationFeatureType.class;
    }

}
