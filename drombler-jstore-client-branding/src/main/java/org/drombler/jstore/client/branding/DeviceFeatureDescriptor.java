package org.drombler.jstore.client.branding;

import org.drombler.commons.client.util.ResourceBundleUtils;
import org.drombler.commons.context.ContextInjector;
import org.drombler.commons.context.ContextManager;
import org.drombler.commons.context.Contexts;
import org.drombler.jstore.client.branding.jaxb.DeviceFeatureType;
import org.osgi.framework.Bundle;
import org.softsmithy.lib.util.Positionable;

import java.lang.reflect.InvocationTargetException;

import static org.drombler.acp.core.commons.util.BundleUtils.loadClass;

public class DeviceFeatureDescriptor<T> implements Positionable {


    private final String displayName;
    private final Class<T> deviceFeatureContentClass;
    private final T deviceFeatureContent;
    private final int position;
    private final boolean selected;


    public DeviceFeatureDescriptor(String displayName, Class<T> deviceFeatureContentClass, T deviceFeatureContent, int position, boolean selected) {
        this.displayName = displayName;
        this.deviceFeatureContentClass = deviceFeatureContentClass;
        this.deviceFeatureContent = deviceFeatureContent;
        this.position = position;
        this.selected = selected;
    }

    /**
     * @return the deviceFeatureContentClass
     */
    public Class<T> getDeviceFeatureContentClass() {
        return deviceFeatureContentClass;
    }


    /**
     * @return the position
     */
    @Override
    public int getPosition() {
        return position;
    }

    public static DeviceFeatureDescriptor<?> createDeviceFeatureDescriptor(DeviceFeatureType deviceFeature, ContextManager contextManager, ContextInjector contextInjector, Bundle bundle) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> deviceFeatureContentClass = loadClass(bundle, deviceFeature.getDeviceFeatureContentClass());
        return createDeviceFeatureDescriptor(deviceFeature, deviceFeatureContentClass, contextManager, contextInjector);
    }

    private static <T> DeviceFeatureDescriptor<T> createDeviceFeatureDescriptor(DeviceFeatureType deviceFeature, Class<T> deviceFeatureContentClass, ContextManager contextManager, ContextInjector contextInjector) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        T deviceFeatureContent = createDeviceFeatureContent(deviceFeatureContentClass, contextManager, contextInjector);
        String displayName = ResourceBundleUtils.getClassResourceStringPrefixed(deviceFeatureContentClass, deviceFeature.getDisplayName());
        return new DeviceFeatureDescriptor<T>(displayName, deviceFeatureContentClass, deviceFeatureContent, deviceFeature.getPosition(), deviceFeature.isSelected());
    }

    private static <T> T createDeviceFeatureContent(Class<T> deviceFeatureContentClass, ContextManager contextManager, ContextInjector contextInjector) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        T deviceFeatureContent = deviceFeatureContentClass.getDeclaredConstructor().newInstance();
        Contexts.configureObject(deviceFeatureContent, contextManager, contextInjector);
        return deviceFeatureContent;
    }
}
