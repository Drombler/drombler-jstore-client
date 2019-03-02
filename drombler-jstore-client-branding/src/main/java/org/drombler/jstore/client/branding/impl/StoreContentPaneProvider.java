package org.drombler.jstore.client.branding.impl;

import javafx.scene.Node;
import org.drombler.acp.core.context.ContextManagerProvider;
import org.drombler.fx.core.application.ApplicationContentProvider;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component
public class StoreContentPaneProvider implements ApplicationContentProvider, ApplicationFeatureBarProvider {

    @Reference
    private ContextManagerProvider contextManagerProvider;

    private final StoreContentPane contentPane = new StoreContentPane();

    @Activate
    protected void activate(ComponentContext context) {
        getApplicationFeatureBar().selectedApplicationFeatureProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                contextManagerProvider.getContextManager().setLocalContextActive(newValue.getApplicationFeatureContent());
            } else {
                contextManagerProvider.getContextManager().setLocalContextActive(null);
            }
        });
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    @Override
    public Node getContentPane() {
        return contentPane;
    }

    @Override
    public ApplicationFeatureBar getApplicationFeatureBar() {
        return contentPane.getApplicationFeatureBar();
    }

}
