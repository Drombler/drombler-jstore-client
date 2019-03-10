package org.drombler.jstore.client.branding.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.jstore.client.branding.ApplicationFeatureDescriptor;

public class StoreContentPane extends BorderPane {
    @FXML
    private ApplicationFeatureBar applicationFeatureBar;

    private ObservableList<ApplicationFeatureDescriptor> appicationFeatureDescriptors = FXCollections.observableArrayList();

    public StoreContentPane() {
        FXMLLoaders.loadRoot(this);
        applicationFeatureBar.selectedApplicationFeatureProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null){
                setCenter(newValue.getApplicationFeatureContent());
            } else {
                setCenter(null);
            }
        });
    }

    public ApplicationFeatureBar getApplicationFeatureBar() {
        return applicationFeatureBar;
    }
}
