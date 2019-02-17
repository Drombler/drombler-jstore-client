package org.drombler.jstore.client.branding.impl;

import javafx.scene.Node;
import org.drombler.acp.core.commons.util.UnresolvedEntry;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.acp.core.context.ContextManagerProvider;
import org.drombler.commons.context.ContextInjector;
import org.drombler.jstore.client.branding.DeviceFeatureDescriptor;
import org.drombler.jstore.client.branding.NavigationBar;
import org.drombler.jstore.client.branding.NavigationBarProvider;
import org.drombler.jstore.client.branding.jaxb.DeviceFeatureType;
import org.drombler.jstore.client.branding.jaxb.DeviceFeaturesType;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softsmithy.lib.util.Positionables;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DeviceFeatureHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceFeatureHandler.class);
    private final List<DeviceFeatureDescriptor<? extends Node>> unresolvedDeviceFeatureDescriptors = new ArrayList<>();
    private final List<UnresolvedEntry<DeviceFeatureType>> unresolvedDeviceFeatures = new ArrayList<>();

    @Reference
    private NavigationBarProvider navigationBarProvider;

    @Reference
    private ApplicationThreadExecutorProvider applicationThreadExecutorProvider;

    @Reference
    private ContextManagerProvider contextManagerProvider;

    private ContextInjector contextInjector;

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindDeviceFeaturesType(ServiceReference<DeviceFeaturesType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        DeviceFeaturesType deviceFeatures = context.getService(serviceReference);
        registerDeviceFeatures(deviceFeatures, context);
    }

    public void unbindDeviceFeaturesType(DeviceFeaturesType deviceFeatures) {
        // TODO
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindDeviceFeatureDescriptor(DeviceFeatureDescriptor<? extends Node> deviceFeatureDescriptor) {
        registerDeviceFeature(deviceFeatureDescriptor);
    }

    public void unbindDeviceFeatureDescriptor(DeviceFeatureDescriptor<?> deviceFeatureDescriptor) {
        // TODO
    }

    @Activate
    protected void activate(ComponentContext context) {
        contextInjector = new ContextInjector(contextManagerProvider.getContextManager());
        resolveUnresolvedDeviceFeatureDescriptors();
        resolveUnresolvedDeviceFeatures();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    private boolean isInitialized() {
        return navigationBarProvider != null && applicationThreadExecutorProvider != null && contextManagerProvider != null && contextInjector != null;
    }

    private void registerDeviceFeatures(DeviceFeaturesType deviceFeatures, BundleContext context) {
        deviceFeatures.getDeviceFeature().forEach(deviceFeature
                -> registerDeviceFeature(deviceFeature, context));
    }

    private void registerDeviceFeature(DeviceFeatureType deviceFeature, BundleContext context) {
        if (isInitialized()) {
            registerDeviceFeatureInitialized(deviceFeature, context);
        } else {
            unresolvedDeviceFeatures.add(new UnresolvedEntry<>(deviceFeature, context));
        }
    }

    private void registerDeviceFeatureInitialized(DeviceFeatureType deviceFeature, BundleContext context) {
        try {
            DeviceFeatureDescriptor<?> deviceFeatureDescriptor = DeviceFeatureDescriptor.createDeviceFeatureDescriptor(deviceFeature, contextManagerProvider.getContextManager(), contextInjector, context.getBundle());
            context.registerService(DeviceFeatureDescriptor.class, deviceFeatureDescriptor, null);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private <T extends Node> void registerDeviceFeature(DeviceFeatureDescriptor<T> deviceFeatureDescriptor) {
        if (isInitialized()) {
            registerDeviceFeatureInitialized(deviceFeatureDescriptor);
        } else {
            unresolvedDeviceFeatureDescriptors.add(deviceFeatureDescriptor);
        }
    }

    private <T extends Node> void registerDeviceFeatureInitialized(DeviceFeatureDescriptor<T> deviceFeatureDescriptor) {
        applicationThreadExecutorProvider.getApplicationThreadExecutor().execute(() -> {
            NavigationBar navigationBar = navigationBarProvider.getNavigationBar();
            int insertionPoint = Positionables.getInsertionPoint(navigationBar.getDeviceFeatures(), deviceFeatureDescriptor);
            navigationBar.getDeviceFeatures().add(insertionPoint, deviceFeatureDescriptor);
        });
    }


    private void resolveUnresolvedDeviceFeatureDescriptors() {
        List<DeviceFeatureDescriptor<? extends Node>> unresolvedDeviceFeatureDescriptorsCopy = new ArrayList<>(unresolvedDeviceFeatureDescriptors);
        unresolvedDeviceFeatureDescriptors.clear();
        unresolvedDeviceFeatureDescriptorsCopy.forEach(this::registerDeviceFeature);
    }

    private void resolveUnresolvedDeviceFeatures() {
        List<UnresolvedEntry<DeviceFeatureType>> unresolvedDeviceFeaturesCopy = new ArrayList<>(unresolvedDeviceFeatures);
        unresolvedDeviceFeatures.clear();
        unresolvedDeviceFeaturesCopy.forEach(entry -> registerDeviceFeature(entry.getEntry(), entry.getContext()));
    }

}
