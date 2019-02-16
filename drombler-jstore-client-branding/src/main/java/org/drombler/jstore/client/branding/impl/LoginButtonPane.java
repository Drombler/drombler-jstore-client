package org.drombler.jstore.client.branding.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.drombler.jstore.client.branding.impl.keycloak.KeycloakLoginDialogDisplayer;
import org.drombler.jstore.client.branding.impl.keycloak.KeycloakLogoutDialogDisplayer;
import org.keycloak.adapters.ServerRequest;
import org.keycloak.adapters.installed.desktop.KeycloakInstalledDesktop;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */


public class LoginButtonPane extends BorderPane {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginButtonPane.class);

    private final KeycloakInstalledDesktop keycloak;
    private final KeycloakLoginDialogDisplayer loginDialogDisplayer;
    private final KeycloakLogoutDialogDisplayer logoutDialogDisplayer;

    public LoginButtonPane() {
        // reads the configuration from classpath: META-INF/keycloak.json
        this.keycloak = new KeycloakInstalledDesktop(NavigationBar.class.getResourceAsStream("/META-INF/keycloak.json"));
        this.keycloak.setLocale(Locale.getDefault(Locale.Category.DISPLAY));

        this.loginDialogDisplayer = new KeycloakLoginDialogDisplayer(keycloak);
        this.logoutDialogDisplayer = new KeycloakLogoutDialogDisplayer(keycloak);
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
    public void login(ActionEvent event) throws VerificationException, IOException, ServerRequest.HttpFailure, InterruptedException, URISyntaxException {

        boolean loginSuccessful = loginDialogDisplayer.showLoginDialog(getScene().getWindow());

        if (loginSuccessful) {
            setCenter(createMenuButton());
            AccessToken token = keycloak.getToken();
// use token to send backend request

// ensure token is valid for at least 30 seconds
            long minValidity = 30L;
            String tokenString = keycloak.getTokenString(minValidity, TimeUnit.SECONDS);
            System.out.println(tokenString);
        }
    }

    private void logout(ActionEvent event) throws VerificationException, IOException, ServerRequest.HttpFailure, InterruptedException, URISyntaxException {
        setCenter(createLoginLink());
        boolean logoutSuccessful = logoutDialogDisplayer.showLogoutDialog(getScene().getWindow());

        if (logoutSuccessful) {
            System.out.println("logged out!");
        }
    }

    private Hyperlink createLoginLink() {
        Hyperlink loginLink = new Hyperlink(ResourceBundleUtils.getClassResourceStringPrefixed(LoginButtonPane.class, "%loginLink.text"));
        loginLink.setOnAction(e -> {
            try {
                login(e);
            } catch (VerificationException | IOException | ServerRequest.HttpFailure | InterruptedException | URISyntaxException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        });
        return loginLink;
    }

    private MenuButton createMenuButton() {
        MenuButton menuButton = new MenuButton(keycloak.getToken().getGivenName() + " " + keycloak.getToken().getFamilyName());
        menuButton.getItems().add(createLogoutMenuItem());
        return menuButton;
    }

    private MenuItem createLogoutMenuItem() {
        MenuItem logoutMenuItem = new MenuItem(ResourceBundleUtils.getClassResourceStringPrefixed(LoginButtonPane.class, "%logoutMenuItem.text"));
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
