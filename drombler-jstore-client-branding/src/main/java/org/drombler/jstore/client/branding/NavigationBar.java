package org.drombler.jstore.client.branding;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.drombler.commons.fx.beans.binding.CollectionBindings;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.jstore.client.data.DeviceToggleButton;
import org.drombler.jstore.client.data.DeviceHandler;

public class NavigationBar extends GridPane {

    @FXML
    private Button backButton;
    @FXML
    private Button forwardButton;
    @FXML
    private HBox deviceHBox;
    @FXML
    private HBox deviceFeatureHBox;
    //    @FXML
//    private DataToggleButton<Device> myComputerButton;
    @FXML
    private MenuButton deviceMenuButton;


    private final ObservableList<DeviceHandler> devices = FXCollections.observableArrayList();
    private final ObservableList<DeviceFeatureDescriptor<? extends Node>> deviceFeatures = FXCollections.observableArrayList();

    private final ToggleGroup deviceToggleGroup = new ToggleGroup();
    private final ToggleGroup deviceFeatureToggleGroup = new ToggleGroup();


    public NavigationBar() {
        FXMLLoaders.loadRoot(this);

        deviceFeatureToggleGroup.getToggles().addListener((ListChangeListener<Toggle>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream()
                            .map(DeviceFeatureToggleButton.class::cast)
                            .filter(deviceFeatureToggleButton -> deviceFeatureToggleButton.getData().isSelected())
                            .findAny() // there should be max. one
                            .ifPresent(deviceFeatureToggleGroup::selectToggle);
                }
            }
        });

        CollectionBindings.bindContent(deviceHBox.getChildren(), devices, DeviceToggleButton::new);
        CollectionBindings.bindContent(deviceToggleGroup.getToggles(), deviceHBox.getChildren(), DeviceToggleButton.class::cast);

        CollectionBindings.bindContent(deviceFeatureHBox.getChildren(), deviceFeatures, DeviceFeatureToggleButton::new);
        CollectionBindings.bindContent(deviceFeatureToggleGroup.getToggles(), deviceFeatureHBox.getChildren(), DeviceFeatureToggleButton.class::cast);
    }

    public ObservableList<DeviceHandler> getDevices() {
        return devices;
    }

    public ObservableList<DeviceFeatureDescriptor<? extends Node>> getDeviceFeatures() {
        return deviceFeatures;
    }

    public ObservableList<Toggle> getDeviceToggles() {
        return deviceToggleGroup.getToggles();
    }

    public ReadOnlyObjectProperty<Toggle> selectedDeviceToggleProperty() {
        return deviceToggleGroup.selectedToggleProperty();
    }

    public ReadOnlyObjectProperty<Toggle> selectedDeviceFeatureToggleProperty() {
        return deviceFeatureToggleGroup.selectedToggleProperty();
    }

}
