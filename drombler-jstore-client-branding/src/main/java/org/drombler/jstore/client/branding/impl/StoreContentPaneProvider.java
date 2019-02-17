package org.drombler.jstore.client.branding.impl;

import javafx.scene.Node;
import org.drombler.fx.core.application.ApplicationContentProvider;
import org.osgi.service.component.annotations.Component;

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
