package org.drombler.jstore.client.branding.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import org.drombler.acp.core.action.spi.ActionRegistry;
import org.drombler.acp.core.action.spi.ActionResolutionManager;
import org.drombler.acp.core.action.spi.ToggleActionDescriptor;
import org.drombler.acp.core.commons.util.UnresolvedEntry;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.acp.core.context.ContextManagerProvider;
import org.drombler.commons.action.fx.FXToggleAction;
import org.drombler.commons.context.ContextInjector;
import org.drombler.jstore.client.branding.ApplicationFeatureDescriptor;
import org.drombler.jstore.client.branding.jaxb.ApplicationFeatureType;
import org.drombler.jstore.client.branding.jaxb.ApplicationFeaturesType;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ApplicationFeatureHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationFeatureHandler.class);
    private final List<UnresolvedEntry<ApplicationFeatureDescriptor<? extends Node>>> unresolvedApplicationFeatureDescriptors = new ArrayList<>();
    private final List<UnresolvedEntry<ApplicationFeatureType>> unresolvedApplicationFeatures = new ArrayList<>();

    @Reference
    private ApplicationFeatureBarProvider applicationFeatureBarProvider;

    @Reference
    private ApplicationThreadExecutorProvider applicationThreadExecutorProvider;

    @Reference
    private ContextManagerProvider contextManagerProvider;

    private ContextInjector contextInjector;

    private ServiceTracker<FXToggleAction, ServiceReference<FXToggleAction>> toggleActionTracker;
    private final ActionRegistry<?> actionRegistry = new ActionRegistry<>(ToggleActionDescriptor.class);
    private final ActionResolutionManager<ApplicationFeatureDescriptor<? extends Node>> actionResolutionManager = new ActionResolutionManager<>();

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindApplicationFeaturesType(ServiceReference<ApplicationFeaturesType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ApplicationFeaturesType applicationFeatures = context.getService(serviceReference);
        registerApplicationFeatures(applicationFeatures, context);
    }

    public void unbindApplicationFeaturesType(ApplicationFeaturesType applicationFeatures) {
        // TODO
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindApplicationFeatureDescriptor(ServiceReference<ApplicationFeatureDescriptor<? extends Node>> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ApplicationFeatureDescriptor<? extends Node> applicationFeatureDescriptor = context.getService(serviceReference);
        registerApplicationFeature(applicationFeatureDescriptor, context);
    }

    public void unbindApplicationFeatureDescriptor(ApplicationFeatureDescriptor<?> applicationFeatureDescriptor) {
        // TODO
    }

    @Activate
    protected void activate(ComponentContext context) {
        toggleActionTracker = createActionTracker(context);
        toggleActionTracker.open();
        contextInjector = new ContextInjector(contextManagerProvider.getContextManager());
        resolveUnresolvedApplicationFeatureDescriptors();
        resolveUnresolvedApplicationFeatures();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        toggleActionTracker.close();
    }

    private boolean isInitialized() {
        return applicationFeatureBarProvider != null && applicationThreadExecutorProvider != null && contextManagerProvider != null && contextInjector != null;
    }

    private ServiceTracker<FXToggleAction, ServiceReference<FXToggleAction>> createActionTracker(ComponentContext context) {
        return new ServiceTracker<>(context.getBundleContext(), FXToggleAction.class,
                new ServiceTrackerCustomizer<FXToggleAction, ServiceReference<FXToggleAction>>() {

                    @Override
                    public ServiceReference<FXToggleAction> addingService(ServiceReference<FXToggleAction> reference) {
                        String actionId = actionRegistry.getActionId(reference);
                        if (actionResolutionManager.containsUnresolvedEntries(actionId)) {
                            actionResolutionManager.removeUnresolvedEntries(actionId).forEach(unresolvedEntry
                                    -> registerApplicationFeatureInitialized(unresolvedEntry.getEntry(), unresolvedEntry.getContext().getService(reference)));
                        }
                        return reference;
                    }

                    @Override
                    public void modifiedService(ServiceReference<FXToggleAction> reference, ServiceReference<FXToggleAction> service) {
                        // TODO ???
                    }

            @Override
            public void removedService(ServiceReference<FXToggleAction> reference, ServiceReference<FXToggleAction> service) {
                // TODO ???
            }
        });
    }

    private void registerApplicationFeatures(ApplicationFeaturesType applicationFeatures, BundleContext context) {
        applicationFeatures.getApplicationFeature().forEach(applicationFeature
                -> registerApplicationFeature(applicationFeature, context));
    }

    private void registerApplicationFeature(ApplicationFeatureType applicationFeature, BundleContext context) {
        if (isInitialized()) {
            registerApplicationFeatureInitialized(applicationFeature, context);
        } else {
            unresolvedApplicationFeatures.add(new UnresolvedEntry<>(applicationFeature, context));
        }
    }

    private void registerApplicationFeatureInitialized(ApplicationFeatureType applicationFeature, BundleContext context) {
        try {
            ApplicationFeatureDescriptor<?> applicationFeatureDescriptor = ApplicationFeatureDescriptor.createApplicationFeatureDescriptor(applicationFeature, contextManagerProvider.
                    getContextManager(), contextInjector, context.getBundle());
            context.registerService(ApplicationFeatureDescriptor.class, applicationFeatureDescriptor, null);

            applicationFeatureDescriptor.getServiceProviderInterfaceClasses().forEach(serviceProviderInterfaceClass
                    -> registerServiceProviderInterface(context, serviceProviderInterfaceClass, applicationFeatureDescriptor.getApplicationFeatureContent())
            );
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private <T> void registerServiceProviderInterface(BundleContext context, Class<T> serviceProviderInterfaceClass, Object serviceProviderInterface) {
        context.registerService(serviceProviderInterfaceClass, serviceProviderInterfaceClass.cast(serviceProviderInterface), null);
    }

    private void registerApplicationFeature(UnresolvedEntry<ApplicationFeatureDescriptor<? extends Node>> unresolvedEntry) {
        registerApplicationFeature(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
    }

    private void registerApplicationFeature(ApplicationFeatureDescriptor<? extends Node> applicationFeatureDescriptor, BundleContext context) {
        if (isInitialized()) {
            final FXToggleAction action = actionRegistry.getAction(applicationFeatureDescriptor.getActionId(), FXToggleAction.class,
                    context);
            if (action != null) {
                registerApplicationFeatureInitialized(applicationFeatureDescriptor, action);
            } else {
                actionResolutionManager.addUnresolvedEntry(applicationFeatureDescriptor.getActionId(),
                        new UnresolvedEntry<>(applicationFeatureDescriptor, context));
            }
        } else {
            unresolvedApplicationFeatureDescriptors.add(new UnresolvedEntry<>(applicationFeatureDescriptor, context));
        }
    }

    private <T extends Node> void registerApplicationFeatureInitialized(ApplicationFeatureDescriptor<T> applicationFeatureDescriptor, FXToggleAction toggleAction) {
        applicationThreadExecutorProvider.getApplicationThreadExecutor().execute(() -> {
            ApplicationFeatureBar applicationFeatureBar = applicationFeatureBarProvider.getApplicationFeatureBar();
            applicationFeatureBar.addApplicationFeatures(applicationFeatureDescriptor, toggleAction);
        });
    }

    private void resolveUnresolvedApplicationFeatureDescriptors() {
        List<UnresolvedEntry<ApplicationFeatureDescriptor<? extends Node>>> unresolvedApplicationFeatureDescriptorsCopy = new ArrayList<>(unresolvedApplicationFeatureDescriptors);
        unresolvedApplicationFeatureDescriptors.clear();
        unresolvedApplicationFeatureDescriptorsCopy.forEach(this::registerApplicationFeature);
    }

    private void resolveUnresolvedApplicationFeatures() {
        List<UnresolvedEntry<ApplicationFeatureType>> unresolvedApplicationFeaturesCopy = new ArrayList<>(unresolvedApplicationFeatures);
        unresolvedApplicationFeatures.clear();
        unresolvedApplicationFeaturesCopy.forEach(entry -> registerApplicationFeature(entry.getEntry(), entry.getContext()));
    }

}
