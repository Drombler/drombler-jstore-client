package org.drombler.jstore.client.branding.impl.keycloak;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.keycloak.OAuthErrorException;
import org.keycloak.adapters.ServerRequest;
import org.keycloak.adapters.installed.desktop.KeycloakInstalledDesktop;
import org.keycloak.common.VerificationException;


public class KeycloakLoginTask extends Task<Void> {

    private static final int TOTAL_WORK = 1;
    private final ResourceBundle resourceBundle = ResourceBundleUtils.getClassResourceBundle(KeycloakLoginTask.class);
    private final KeycloakInstalledDesktop keycloak;

    // opens desktop browser
    public KeycloakLoginTask(KeycloakInstalledDesktop keycloak) {
        this.keycloak = keycloak;
        updateTitle(resourceBundle.getString("title"));
        updateMessage(resourceBundle.getString("message.initializing"));
        updateProgress(0, TOTAL_WORK);
    }

    @Override
    protected Void call() throws IOException, VerificationException, OAuthErrorException, URISyntaxException, ServerRequest.HttpFailure, InterruptedException {
        updateMessage(resourceBundle.getString("message.login"));
        updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, TOTAL_WORK);
        
        // opens desktop browser
        keycloak.login();
        
        return null;
    }
}

