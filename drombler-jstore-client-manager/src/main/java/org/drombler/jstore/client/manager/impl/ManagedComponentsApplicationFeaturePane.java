package org.drombler.jstore.client.manager.impl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.jstore.client.branding.ApplicationFeature;
import org.drombler.jstore.client.branding.impl.NavigationBar;

import static org.drombler.jstore.client.manager.impl.ManagedComponentsApplicationFeatureToggleAction.ID;

/**
 *
 * @author puce
 */

@ApplicationFeature(actionId = ID, position = 10)
public class ManagedComponentsApplicationFeaturePane extends BorderPane {
    @FXML
private NavigationBar navigationBar;

    public ManagedComponentsApplicationFeaturePane(){
        FXMLLoaders.loadRoot(this);
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }
}
