package org.drombler.jstore.client.branding.impl.keycloak;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Window;
import org.keycloak.adapters.installed.KeycloakInstalled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
public class KeycloakLogoutDialogDisplayer {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakLogoutDialogDisplayer.class);

    private final ExecutorService executorService = Executors.newCachedThreadPool(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
//        thread.setName
        return thread;
    });

    private final KeycloakInstalled keycloakInstalled;

    public KeycloakLogoutDialogDisplayer(KeycloakInstalled keycloakInstalled) {
        this.keycloakInstalled = keycloakInstalled;
    }

    public boolean showLogoutDialog(Window owner) {
        KeycloakLogoutTask keycloakLogoutTask = new KeycloakLogoutTask(keycloakInstalled);
        Dialog<ButtonType> dialog = createKeycloakLogoutDialog(owner, keycloakLogoutTask);

        keycloakLogoutTask.setOnSucceeded(event -> {
            dialog.setResult(ButtonType.OK);
        });
        keycloakLogoutTask.setOnFailed(event -> {
            String errorMessage = "Login failed!";
            Throwable exception = keycloakLogoutTask.getException();
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

    private Dialog<ButtonType> createKeycloakLogoutDialog(Window owner, KeycloakLogoutTask keycloakLogoutTask) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(owner);
        
        dialog.titleProperty().bind(keycloakLogoutTask.titleProperty());
        dialog.setOnShown(event -> executorService.execute(keycloakLogoutTask));

        dialog.setResizable(true);

        KeycloakTaskPane keycloakLoginPane = new KeycloakTaskPane();
        keycloakLoginPane.messageProperty().bind(keycloakLogoutTask.messageProperty());
        keycloakLoginPane.progressProperty().bind(keycloakLogoutTask.progressProperty());
        dialog.getDialogPane().setContent(keycloakLoginPane);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setDefaultButton(true);
        return dialog;
    }

}
