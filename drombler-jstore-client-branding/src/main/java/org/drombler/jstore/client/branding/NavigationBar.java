package org.drombler.jstore.client.branding;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.drombler.commons.fx.beans.binding.CollectionBindings;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.jstore.client.branding.impl.keycloak.KeycloakLoginDialogDisplayer;
import org.drombler.jstore.client.branding.impl.keycloak.KeycloakLogoutDialogDisplayer;
import org.drombler.jstore.client.data.DeviceHandler;
import org.drombler.jstore.client.data.DeviceToggleButton;
import org.keycloak.adapters.ServerRequest;
import org.keycloak.adapters.installed.KeycloakInstalled;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavigationBar extends GridPane {
    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationBar.class);

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

    @FXML
    private BorderPane loginPane;

    @FXML
    private Hyperlink loginLink;

    private final ObservableList<DeviceHandler> devices = FXCollections.observableArrayList();
    private final ObservableList<DeviceFeatureDescriptor<? extends Node>> deviceFeatures = FXCollections.observableArrayList();

    private final ToggleGroup deviceToggleGroup = new ToggleGroup();
    private final ToggleGroup deviceFeatureToggleGroup = new ToggleGroup();

    private final KeycloakInstalled keycloak;
    private final KeycloakLoginDialogDisplayer loginDialogDisplayer;
    private final KeycloakLogoutDialogDisplayer logoutDialogDisplayer;


    public NavigationBar() {
        FXMLLoaders.loadRoot(this);

        // reads the configuration from classpath: META-INF/keycloak.json
        this.keycloak = new KeycloakInstalled(NavigationBar.class.getResourceAsStream("/META-INF/keycloak.json"));
        this.keycloak.setLocale(Locale.getDefault(Locale.Category.DISPLAY));

        this.loginDialogDisplayer = new KeycloakLoginDialogDisplayer(keycloak);
        this.logoutDialogDisplayer = new KeycloakLogoutDialogDisplayer(keycloak);

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

    /**
     * See: https://www.keycloak.org/docs/latest/securing_apps/index.html#_installed_adapter
     *
     * @param event
     * @throws VerificationException
     * @throws IOException
     * @throws InterruptedException
     * @throws ServerRequest.HttpFailure
     * @throws URISyntaxException
     */
    public void login(ActionEvent event) throws VerificationException, IOException, ServerRequest.HttpFailure, InterruptedException, URISyntaxException {

        boolean loginSuccessful = loginDialogDisplayer.showLoginDialog(getScene().getWindow());

        if (loginSuccessful) {
            loginPane.setCenter(createMenuButton());
            AccessToken token = keycloak.getToken();
// use token to send backend request

// ensure token is valid for at least 30 seconds
            long minValidity = 30L;
            String tokenString = keycloak.getTokenString(minValidity, TimeUnit.SECONDS);
            System.out.println(tokenString);
        }
    }

    private void logout(ActionEvent event) throws VerificationException, IOException, ServerRequest.HttpFailure, InterruptedException, URISyntaxException {
        loginPane.setCenter(loginLink);
        boolean logoutSuccessful = logoutDialogDisplayer.showLogoutDialog(getScene().getWindow());

        if (logoutSuccessful) {
            System.out.println("logged out!");
        }
    }

    private MenuButton createMenuButton() {
        MenuButton menuButton = new MenuButton(keycloak.getToken().getGivenName() + " " + keycloak.getToken().getFamilyName());
        menuButton.getItems().add(createLogoutMenuItem());
        return menuButton;
    }

    private MenuItem createLogoutMenuItem() {
        MenuItem logoutMenuItem = new MenuItem(ResourceBundleUtils.getClassResourceStringPrefixed(NavigationBar.class, "%logoutMenuItem.text"));
        logoutMenuItem.setOnAction(e -> {
            try {
                logout(e);
            } catch (VerificationException | IOException | ServerRequest.HttpFailure | InterruptedException | URISyntaxException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        });
        return logoutMenuItem;
    }
}
