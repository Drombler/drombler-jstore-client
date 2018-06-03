package org.drombler.jstore.client.branding.impl;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import org.drombler.acp.core.context.ContextManagerProvider;
import org.drombler.commons.context.ContextInjector;
import org.drombler.fx.core.application.ApplicationContentProvider;
import org.drombler.jstore.client.data.DeviceHandlerListProvider;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component
public class StoreContentPaneProvider implements ApplicationContentProvider, NavigationBarProvider {
    private final StoreContentPane contentPane = new StoreContentPane();
    @Reference
    private ContextManagerProvider contextManagerProvider;
    @Reference
    private DeviceHandlerListProvider deviceHandlerListProvider;

    private ContextInjector contextInjector;

    @Activate
    protected void activate(ComponentContext context) {
        Bindings.bindContentBidirectional(getNavigationBar().getDevices(), deviceHandlerListProvider.getDeviceHandlers());
        getNavigationBar().getDeviceToggles().addListener((ListChangeListener<Toggle>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(contextManagerProvider.getContextManager()::removeLocalContext);
                } else if (change.wasAdded()) {
                    change.getAddedSubList().forEach(contextManagerProvider.getContextManager()::putLocalContext);
                }
            }
        });
        getNavigationBar().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            contextManagerProvider.getContextManager().setLocalContextActive(newValue);
        });
    }


    @Deactivate
    protected void deactivate(ComponentContext context) {
        Bindings.unbindContentBidirectional(getNavigationBar().getDevices(), deviceHandlerListProvider.getDeviceHandlers());
    }

    @Override
    public Node getContentPane() {
        return contentPane;
    }

    @Override
    public NavigationBar getNavigationBar() {
        return contentPane.getNavigationBar();
    }
}
