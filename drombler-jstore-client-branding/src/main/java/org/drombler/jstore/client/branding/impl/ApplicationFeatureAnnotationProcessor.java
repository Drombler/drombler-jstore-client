package org.drombler.jstore.client.branding.impl;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
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
        List<String> serviceProviderInterfaceClassNames = convertClassArrayAnnotationValueToClassNames(element.getAnnotationMirrors(), ApplicationFeature.class, "serviceProviderInterfaces");
        if (!serviceProviderInterfaceClassNames.isEmpty()) {
            applicationFeature.getServiceProviderInterfaceClasses().addAll(serviceProviderInterfaceClassNames);
        }
        applicationFeature.setApplicationFeatureContentClass(element.asType().toString());
        applicationFeatures.getApplicationFeature().add(applicationFeature);
    }

    private List<String> convertClassArrayAnnotationValueToClassNames(List<? extends AnnotationMirror> annotationMirrors, Class<? extends Annotation> annotationClass, String memberName) {
        return annotationMirrors.stream()
                .filter(annotationMirror -> annotationMirror.getAnnotationType().toString().equals(annotationClass.getName()))
                .map(AnnotationMirror::getElementValues)
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> entry.getKey().getSimpleName().contentEquals(memberName))
                .map(Map.Entry::getValue)
                .flatMap(annotationValue -> ((List<? extends AnnotationValue>) annotationValue.getValue()).stream())
                .map(annotationValue -> (TypeMirror) annotationValue.getValue())
                .map(TypeMirror::toString)
                .collect(Collectors.toList());
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
