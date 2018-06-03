package org.drombler.jstore.client.branding.impl;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.drombler.commons.fx.beans.binding.CollectionBindings;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.jstore.client.branding.DeviceFeature;
import org.drombler.jstore.client.data.DeviceHandler;
import org.drombler.jstore.client.model.json.DeviceConfiguration;
import org.drombler.jstore.client.protocol.json.Store;

import java.util.ArrayList;
import java.util.List;

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
    private final ObservableList<DeviceFeature> deviceFeatures = FXCollections.observableArrayList();

    private final ToggleGroup deviceToggleGroup = new ToggleGroup();
    private final ToggleGroup deviceFeatureToggleGroup = new ToggleGroup();

    public NavigationBar() {
        FXMLLoaders.loadRoot(this);

        CollectionBindings.bindContent(deviceHBox.getChildren(), devices, DeviceToggleButton::new);
        CollectionBindings.bindContent(deviceToggleGroup.getToggles(), deviceHBox.getChildren(), DeviceToggleButton.class::cast);
        deviceToggleGroup.getToggles().addListener((ListChangeListener<Toggle>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream()
                            .map(DeviceToggleButton.class::cast)
                            .filter(deviceToggleButton -> deviceToggleButton.getData().isMyComputer())
                            .findAny()
                            .ifPresent(deviceToggleGroup::selectToggle);
                }
            }
        });
    }


    // TODO: read from deviceConfigurations.json file
    private DeviceConfiguration getMyComupterDeviceConfiguration() {
        DeviceConfiguration myComputerDeviceConfiguration = new DeviceConfiguration();
        myComputerDeviceConfiguration.setDisplayName("myComputer");
        myComputerDeviceConfiguration.setPort(42731);
        return myComputerDeviceConfiguration;
    }

    // TODO: get stores from device
    private List<Store> getStores() {
        Store store = new Store();
        store.setId("jstore");
        store.setDisplayName("JStore");
        store.setEndpoint("http://drombler-jstore-staging.us-east-1.elasticbeanstalk.com/webresources");
        List<Store> stores = new ArrayList<>();
        stores.add(store);
        return stores;
    }

    public ObservableList<DeviceHandler> getDevices() {
        return devices;
    }

    public ObservableList<DeviceFeature> getDeviceFeatures() {
        return deviceFeatures;
    }

    public ObservableList<Toggle> getDeviceToggles() {
        return deviceToggleGroup.getToggles();
    }

    public ReadOnlyObjectProperty<Toggle> selectedToggleProperty() {
        return deviceToggleGroup.selectedToggleProperty();
    }
}
