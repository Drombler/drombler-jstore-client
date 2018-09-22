package org.drombler.jstore.client.branding.impl;

import org.drombler.acp.core.application.processing.AbstractApplicationAnnotationProcessor;
import org.drombler.jstore.client.branding.DeviceFeature;
import org.drombler.jstore.client.branding.jaxb.DeviceFeatureType;
import org.drombler.jstore.client.branding.jaxb.DeviceFeaturesType;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes({"org.drombler.jstore.client.branding.DeviceFeature"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DeviceFeatureAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private DeviceFeaturesType deviceFeatures;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(DeviceFeature.class).forEach(element -> {
            DeviceFeature deviceFeatureAnnotation = element.getAnnotation(DeviceFeature.class);
            if (deviceFeatureAnnotation != null) {
                registerDeviceFeature(deviceFeatureAnnotation, element);
            }
        });
        return false;
    }

    private void registerDeviceFeature(DeviceFeature deviceFeatureAnnotation, Element element) {
        init(element);

        DeviceFeatureType deviceFeature = new DeviceFeatureType();
        deviceFeature.setDisplayName(deviceFeatureAnnotation.displayName());
        deviceFeature.setPosition(deviceFeatureAnnotation.position());
        deviceFeature.setSelected(deviceFeatureAnnotation.selected());
        deviceFeature.setDeviceFeatureContentClass(element.asType().toString());
        deviceFeatures.getDeviceFeature().add(deviceFeature);
    }

    private void init(Element element) {
        if (deviceFeatures == null) {
            deviceFeatures = new DeviceFeaturesType();
            addExtensionConfigurations(deviceFeatures);
            addJAXBRootClasses(DeviceFeaturesType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

}
