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
public class StoreContentPaneProvider implements ApplicationContentProvider, ApplicationFeatureBarProvider {

    private final StoreContentPane contentPane = new StoreContentPane();

    @Override
    public Node getContentPane() {
        return contentPane;
    }

    @Override
    public ApplicationFeatureBar getApplicationFeatureBar() {
        return contentPane.getApplicationFeatureBar();
    }


}
