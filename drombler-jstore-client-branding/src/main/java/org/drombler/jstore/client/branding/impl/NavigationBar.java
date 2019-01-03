package org.drombler.jstore.client.branding.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.drombler.commons.fx.beans.binding.CollectionBindings;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.jstore.client.branding.DeviceFeatureDescriptor;
import org.drombler.jstore.client.branding.impl.keycloak.KeycloakLoginDialog;
import org.drombler.jstore.client.data.DeviceHandler;
import org.keycloak.OAuthErrorException;
import org.keycloak.adapters.ServerRequest;
import org.keycloak.adapters.installed.desktop.KeycloakInstalledDesktop;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;

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

    @FXML
    private Hyperlink loginLink;

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

    /**
     * See: https://www.keycloak.org/docs/latest/securing_apps/index.html#_installed_adapter
     *
     * @param event
     * @throws VerificationException
     * @throws IOException
     * @throws InterruptedException
     * @throws ServerRequest.HttpFailure
     * @throws URISyntaxException
     * @throws OAuthErrorException
     */
    public void login(ActionEvent event) throws VerificationException, IOException, InterruptedException, ServerRequest.HttpFailure, URISyntaxException, OAuthErrorException {
        // reads the configuration from classpath: META-INF/keycloak.json
        KeycloakInstalledDesktop keycloak = new KeycloakInstalledDesktop(NavigationBar.class.getResourceAsStream("/META-INF/keycloak.json"));
        keycloak.setLocale(Locale.getDefault(Locale.Category.DISPLAY));

        KeycloakLoginDialog loginDialog = new KeycloakLoginDialog();
        boolean successful = loginDialog.login(keycloak);

        if (successful) {
            AccessToken token = keycloak.getToken();
// use token to send backend request

// ensure token is valid for at least 30 seconds
            long minValidity = 30L;
            String tokenString = keycloak.getTokenString(minValidity, TimeUnit.SECONDS);
            System.out.println(tokenString);
        }
    }
}
