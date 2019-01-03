package org.drombler.jstore.client.branding.impl.keycloak;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.keycloak.OAuthErrorException;
import org.keycloak.adapters.ServerRequest.HttpFailure;
import org.keycloak.adapters.installed.desktop.KeycloakInstalledDesktop;
import org.keycloak.common.VerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
public class KeycloakLoginDialog {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakLoginDialog.class);

    private final ExecutorService executorService = Executors.newCachedThreadPool(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
//        thread.setName
        return thread;
    });
    private final ResourceBundle resourceBundle = ResourceBundleUtils.getClassResourceBundle(KeycloakLoginDialog.class);

    public boolean login(KeycloakInstalledDesktop keycloakInstalledDesktop) {
        KeycloakLoginTask keycloakLoginTask = new KeycloakLoginTask(keycloakInstalledDesktop);
        Dialog<ButtonType> dialog = createKeycloakLoginDialog(keycloakLoginTask);

        keycloakLoginTask.setOnSucceeded(event -> {
            dialog.setResult(ButtonType.OK);
        });
        keycloakLoginTask.setOnFailed(event -> {
            String errorMessage = "Login failed!";
            Throwable exception = keycloakLoginTask.getException();
            if (exception != null) {
                LOG.error(errorMessage, exception);
//                throw exception;
            } else {
                LOG.error(errorMessage);
            }
            // TODO: show error dialog
        });

        return dialog.showAndWait()
                .filter(result -> result == ButtonType.OK)
                .isPresent();
    }

    private Dialog<ButtonType> createKeycloakLoginDialog(KeycloakLoginTask keycloakLoginTask) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.titleProperty().bind(keycloakLoginTask.titleProperty());
        dialog.setOnShown(event -> executorService.execute(keycloakLoginTask));

        dialog.setResizable(true);

        KeycloakLoginPane keycloakLoginPane = new KeycloakLoginPane();
        keycloakLoginPane.messageProperty().bind(keycloakLoginTask.messageProperty());
        keycloakLoginPane.progressProperty().bind(keycloakLoginTask.progressProperty());
        dialog.getDialogPane().setContent(keycloakLoginPane);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setDefaultButton(true);
        return dialog;
    }

    private class KeycloakLoginTask extends Task<Void> {

        private static final int TOTAL_WORK = 1;

        private final KeycloakInstalledDesktop keycloak;

        public KeycloakLoginTask(KeycloakInstalledDesktop keycloak) {
            this.keycloak = keycloak;
            updateTitle(resourceBundle.getString("keycloakLoginTask.title"));
            updateMessage(resourceBundle.getString("keycloakLoginTask.message.initializing"));
            updateProgress(0, TOTAL_WORK);
        }

        @Override
        protected Void call() throws IOException, VerificationException, OAuthErrorException, URISyntaxException, HttpFailure, InterruptedException {
            updateMessage(resourceBundle.getString("keycloakLoginTask.message.login"));
            updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, TOTAL_WORK);

            // opens desktop browser
            keycloak.login();

            return null;
        }

    }
}
