package org.drombler.jstore.client.branding.impl;

import javafx.scene.Node;
import org.drombler.fx.core.application.ApplicationContentProvider;
import org.osgi.service.component.annotations.Component;

@Component
public class StoreContentPaneProvider implements ApplicationContentProvider {
    private final StoreContentPane contentPane = new StoreContentPane();

    @Override
    public Node getContentPane() {
        return contentPane;
    }
}
