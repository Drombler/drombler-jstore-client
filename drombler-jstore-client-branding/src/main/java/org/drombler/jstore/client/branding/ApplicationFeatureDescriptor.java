package org.drombler.jstore.client.branding;

import java.lang.reflect.InvocationTargetException;
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
    private final T applicationFeatureContent;
    private final int position;
//    private final boolean selected;


    public ApplicationFeatureDescriptor(String actionId, Class<T> applicationFeatureContentClass, T applicationFeatureContent, int position) {//, boolean selected) {
        this.actionId = actionId;
        this.applicationFeatureContentClass = applicationFeatureContentClass;
        this.applicationFeatureContent = applicationFeatureContent;
        this.position = position;
//        this.selected = selected;
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


    public static ApplicationFeatureDescriptor<?> createApplicationFeatureDescriptor(ApplicationFeatureType applicationFeature, ContextManager contextManager, ContextInjector contextInjector,
            Bundle bundle) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<? extends Node> applicationFeatureContentClass = (Class<? extends Node>) loadClass(bundle, applicationFeature.getApplicationFeatureContentClass());
        return createApplicationFeatureDescriptor(applicationFeature, applicationFeatureContentClass, contextManager, contextInjector);
    }

    private static <T extends Node> ApplicationFeatureDescriptor<T> createApplicationFeatureDescriptor(ApplicationFeatureType applicationFeature, Class<T> applicationFeatureContentClass,
            ContextManager contextManager,
            ContextInjector contextInjector) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        T applicationFeatureContent = createApplicationFeatureContent(applicationFeatureContentClass, contextManager, contextInjector);
        return new ApplicationFeatureDescriptor<>(applicationFeature.getActionId(), applicationFeatureContentClass, applicationFeatureContent, applicationFeature.getPosition()); //, deviceFeature.isSelected());
    }

    private static <T extends Node> T createApplicationFeatureContent(Class<T> applicationFeatureContentClass, ContextManager contextManager, ContextInjector contextInjector) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        T applicationFeatureContent = applicationFeatureContentClass.getDeclaredConstructor().newInstance();
        Contexts.configureObject(applicationFeatureContent, contextManager, contextInjector);
        return applicationFeatureContent;
    }

}
