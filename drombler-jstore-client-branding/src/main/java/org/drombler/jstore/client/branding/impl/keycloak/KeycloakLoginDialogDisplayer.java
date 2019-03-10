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
public class KeycloakLoginDialogDisplayer {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakLoginDialogDisplayer.class);

    private final ExecutorService executorService = Executors.newCachedThreadPool(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
//        thread.setName
        return thread;
    });

    private final KeycloakInstalled keycloakInstalled;

    public KeycloakLoginDialogDisplayer(KeycloakInstalled keycloakInstalled) {
        this.keycloakInstalled = keycloakInstalled;
    }

    public boolean showLoginDialog(Window owner) {
        KeycloakLoginTask keycloakLoginTask = new KeycloakLoginTask(keycloakInstalled);
        Dialog<ButtonType> dialog = createKeycloakLoginDialog(owner, keycloakLoginTask);

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

    private Dialog<ButtonType> createKeycloakLoginDialog(Window owner, KeycloakLoginTask keycloakLoginTask) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(owner);
        
        dialog.titleProperty().bind(keycloakLoginTask.titleProperty());
        dialog.setOnShown(event -> executorService.execute(keycloakLoginTask));

        dialog.setResizable(true);

        KeycloakTaskPane keycloakLoginPane = new KeycloakTaskPane();
        keycloakLoginPane.messageProperty().bind(keycloakLoginTask.messageProperty());
        keycloakLoginPane.progressProperty().bind(keycloakLoginTask.progressProperty());
        dialog.getDialogPane().setContent(keycloakLoginPane);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setDefaultButton(true);
        return dialog;
    }

}
