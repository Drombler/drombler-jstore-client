package org.drombler.jstore.client.branding.impl;

import javafx.scene.layout.BorderPane;
import org.drombler.commons.fx.fxml.FXMLLoaders;

public class StoreContentPane extends BorderPane {
    public StoreContentPane() {
        FXMLLoaders.loadRoot(this);
    }
}
