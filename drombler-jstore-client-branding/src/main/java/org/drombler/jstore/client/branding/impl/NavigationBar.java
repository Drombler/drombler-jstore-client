package org.drombler.jstore.client.branding.impl;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.drombler.commons.fx.beans.binding.CollectionBindings;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.jstore.client.branding.DeviceFeatureDescriptor;
import org.drombler.jstore.client.data.DeviceHandler;

public class NavigationBar extends GridPane {
    @FXML
    private Button backButton;
    @FXML
    private Button forwardButton;
    @FXML
    private HBox deviceHBox;
    @FXML
    private HBox featureHBox;
    //    @FXML
//    private DataToggleButton<Device> myComputerButton;
    @FXML
    private MenuButton deviceMenuButton;

    private final ObservableList<DeviceHandler> devices = FXCollections.observableArrayList();
    private final ObservableList<DeviceFeatureDescriptor<? extends Node>> features = FXCollections.observableArrayList();

    private final ToggleGroup deviceToggleGroup = new ToggleGroup();
    private final ToggleGroup featureToggleGroup = new ToggleGroup();

    public NavigationBar() {
        FXMLLoaders.loadRoot(this);

        featureToggleGroup.getToggles().addListener((ListChangeListener<Toggle>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream()
                            .map(DeviceFeatureToggleButton.class::cast)
                            .filter(deviceFeatureToggleButton -> deviceFeatureToggleButton.getData().isSelected())
                            .findAny() // there should be max. one
                            .ifPresent(featureToggleGroup::selectToggle);
                }
            }
        });

        CollectionBindings.bindContent(deviceHBox.getChildren(), devices, DeviceToggleButton::new);
        CollectionBindings.bindContent(deviceToggleGroup.getToggles(), deviceHBox.getChildren(), DeviceToggleButton.class::cast);

        CollectionBindings.bindContent(featureHBox.getChildren(), features, DeviceFeatureToggleButton::new);
        CollectionBindings.bindContent(featureToggleGroup.getToggles(), featureHBox.getChildren(), DeviceFeatureToggleButton.class::cast);
    }



    public ObservableList<DeviceHandler> getDevices() {
        return devices;
    }

    public ObservableList<DeviceFeatureDescriptor<? extends Node>> getFeatures() {
        return features;
    }

    public ObservableList<Toggle> getDeviceToggles() {
        return deviceToggleGroup.getToggles();
    }

    public ReadOnlyObjectProperty<Toggle> selectedDeviceToggleProperty() {
        return deviceToggleGroup.selectedToggleProperty();
    }

    public ReadOnlyObjectProperty<Toggle> selectedFeatureToggleProperty() {
        return featureToggleGroup.selectedToggleProperty();
    }
}
