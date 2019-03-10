package org.drombler.jstore.client.manager.impl;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Toggle;
import org.drombler.acp.core.context.ContextManagerProvider;
import org.drombler.jstore.client.branding.NavigationBar;
import org.drombler.jstore.client.branding.NavigationBarProvider;
import org.drombler.jstore.client.data.DeviceHandlerListProvider;
import org.drombler.jstore.client.data.DeviceToggleButton;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component
public class ManagedComponentContentPaneProvider {

//    private final ManagedComponentsApplicationFeaturePane contentPane = new ManagedComponentsApplicationFeaturePane();

    @Reference
    private ContextManagerProvider contextManagerProvider;

    @Reference
    private DeviceHandlerListProvider deviceHandlerListProvider;

    @Reference
    private NavigationBarProvider navigationBarProvider;

//    private ContextInjector contextInjector;

    @Activate
    protected void activate(ComponentContext context) {
//        contextInjector = new ContextInjector(contextManagerProvider.getContextManager());
//        Contexts.configureObject(contentPane, contextManagerProvider.getContextManager(), contextInjector);

        getNavigationBar().getDeviceToggles().addListener((ListChangeListener<Toggle>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(contextManagerProvider.getContextManager()::removeLocalContext);
                } else if (change.wasAdded()) {
                        change.getAddedSubList().stream()
                                .map(DeviceToggleButton.class::cast)
                                .forEach(deviceToggleButton -> {
                                    contextManagerProvider.getContextManager().putLocalContext(deviceToggleButton);
                                    deviceToggleButton.setSelected(deviceToggleButton.getData().isMyComputer());
                                });
                    }
            }
        });

        Bindings.bindContentBidirectional(getNavigationBar().getDevices(), deviceHandlerListProvider.getDeviceHandlers());
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        Bindings.unbindContentBidirectional(getNavigationBar().getDevices(), deviceHandlerListProvider.getDeviceHandlers());
    }

    private NavigationBar getNavigationBar() {
        return navigationBarProvider.getNavigationBar();
    }

}
