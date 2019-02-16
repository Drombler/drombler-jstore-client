package org.drombler.jstore.client.branding.impl;

import com.jfoenix.controls.JFXDrawer;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import org.drombler.commons.fx.fxml.FXMLLoaders;

public class StoreContentPane extends BorderPane {

    @FXML
    private NavigationBar navigationBar;

    @FXML
    private JFXDrawer drawer;

    public StoreContentPane() {
        FXMLLoaders.loadRoot(this);
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }
}
