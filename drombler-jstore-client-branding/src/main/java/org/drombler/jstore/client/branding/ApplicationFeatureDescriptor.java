package org.drombler.jstore.client.branding;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Node;
import static org.drombler.acp.core.commons.util.BundleUtils.loadClass;
import org.drombler.commons.context.ContextInjector;
import org.drombler.commons.context.ContextManager;
import org.drombler.commons.context.Contexts;
import org.drombler.jstore.client.branding.jaxb.ApplicationFeatureType;
import org.osgi.framework.Bundle;
import org.softsmithy.lib.util.Positionable;

public class ApplicationFeatureDescriptor<T extends Node> implements Positionable {

    private final String actionId;
    private final Class<T> applicationFeatureContentClass;
    private final List<Class<?>> serviceProviderInterfaceClasses;
    private final T applicationFeatureContent;
    private final int position;
//    private final boolean selected;

    public ApplicationFeatureDescriptor(String actionId, Class<T> applicationFeatureContentClass, T applicationFeatureContent, int position, List<Class<?>> serviceProviderInterfaceClasses) {//, boolean selected) {
        this.actionId = actionId;
        this.applicationFeatureContentClass = applicationFeatureContentClass;
        this.applicationFeatureContent = applicationFeatureContent;
        this.position = position;
//        this.selected = selected;
        this.serviceProviderInterfaceClasses = serviceProviderInterfaceClasses;
    }

    public String getActionId() {
        return actionId;
    }

    /**
     * @return the deviceFeatureContentClass
     */
    public Class<T> getApplicationFeatureContentClass() {
        return applicationFeatureContentClass;
    }

    public T getApplicationFeatureContent() {
        return applicationFeatureContent;
    }

    @Override
    public int getPosition() {
        return position;
    }

//    public boolean isSelected() {
//        return selected;
//    }
    /**
     * @return the serviceProviderInterfaceClasses
     */
    public List<Class<?>> getServiceProviderInterfaceClasses() {
        return serviceProviderInterfaceClasses;
    }

    public static ApplicationFeatureDescriptor<?> createApplicationFeatureDescriptor(ApplicationFeatureType applicationFeature, ContextManager contextManager, ContextInjector contextInjector,
            Bundle bundle) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<? extends Node> applicationFeatureContentClass = (Class<? extends Node>) loadClass(bundle, applicationFeature.getApplicationFeatureContentClass());
        List<Class<?>> serviceProviderInterfaceClasses = convertServiceProviderInterfaceClasses(applicationFeature, bundle);
        return createApplicationFeatureDescriptor(applicationFeature, applicationFeatureContentClass, serviceProviderInterfaceClasses, contextManager, contextInjector);
    }

    private static <T extends Node> ApplicationFeatureDescriptor<T> createApplicationFeatureDescriptor(ApplicationFeatureType applicationFeature, Class<T> applicationFeatureContentClass,
            List<Class<?>> serviceProviderInterfaceClasses,
            ContextManager contextManager,
            ContextInjector contextInjector) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        T applicationFeatureContent = createApplicationFeatureContent(applicationFeatureContentClass, contextManager, contextInjector);
        return new ApplicationFeatureDescriptor<>(applicationFeature.getActionId(), applicationFeatureContentClass, applicationFeatureContent, applicationFeature.getPosition(),
                serviceProviderInterfaceClasses); //, deviceFeature.isSelected());
    }

    private static <T extends Node> T createApplicationFeatureContent(Class<T> applicationFeatureContentClass, ContextManager contextManager, ContextInjector contextInjector) throws
            NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        T applicationFeatureContent = applicationFeatureContentClass.getDeclaredConstructor().newInstance();
        Contexts.configureObject(applicationFeatureContent, contextManager, contextInjector);
        return applicationFeatureContent;
    }

    private static List<Class<?>> convertServiceProviderInterfaceClasses(ApplicationFeatureType applicationFeature, Bundle bundle) {
        return applicationFeature.getServiceProviderInterfaceClasses().stream()
                .map(className -> {
                    try {
                        return loadClass(bundle, className);
                    } catch (ClassNotFoundException ex) {
                        throw new IllegalArgumentException(ex.getMessage(), ex);
                    }
                })
                .collect(Collectors.toList());
    }

}
