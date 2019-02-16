package org.drombler.jstore.client.branding.impl;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.drombler.acp.core.application.processing.AbstractApplicationAnnotationProcessor;
import org.drombler.jstore.client.branding.ApplicationFeature;
import org.drombler.jstore.client.branding.jaxb.ApplicationFeatureType;
import org.drombler.jstore.client.branding.jaxb.ApplicationFeaturesType;

@SupportedAnnotationTypes({"org.drombler.jstore.client.branding.ApplicationFeature"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ApplicationFeatureAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private ApplicationFeaturesType applicationFeatures;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(ApplicationFeature.class).forEach(element -> {
            ApplicationFeature applicationFeatureAnnotation = element.getAnnotation(ApplicationFeature.class);
            if (applicationFeatureAnnotation != null) {
                registerApplicationFeature(applicationFeatureAnnotation, element);
            }
        });
        return false;
    }

    private void registerApplicationFeature(ApplicationFeature applicationFeatureAnnotation, Element element) {
        init(element);

        ApplicationFeatureType applicationFeature = new ApplicationFeatureType();
        applicationFeature.setActionId(applicationFeatureAnnotation.actionId());
        applicationFeature.setPosition(applicationFeatureAnnotation.position());
//        applicationFeature.setSelected(applicationFeatureAnnotation.selected());
        applicationFeature.setApplicationFeatureContentClass(element.asType().toString());
        applicationFeatures.getApplicationFeature().add(applicationFeature);
    }

    private void init(Element element) {
        if (applicationFeatures == null) {
            applicationFeatures = new ApplicationFeaturesType();
            addExtensionConfigurations(applicationFeatures);
            addJAXBRootClasses(ApplicationFeaturesType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

}
