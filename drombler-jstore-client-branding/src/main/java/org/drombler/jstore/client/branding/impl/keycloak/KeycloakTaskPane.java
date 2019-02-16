package org.drombler.jstore.client.branding.impl.keycloak;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import org.drombler.commons.fx.fxml.FXMLLoaders;

/**
 *
 * @author puce
 */
public class KeycloakTaskPane extends GridPane {

    @FXML
    private Label messageLabel;

    @FXML
    private ProgressBar progressBar;

    public KeycloakTaskPane() {
        FXMLLoaders.loadRoot(this);
    }

    public final String getMessage() {
        return messageLabel.getText();
    }

    public final void setMessage(String message) {
        messageLabel.setText(message);
    }

    public final StringProperty messageProperty() {
        return messageLabel.textProperty();
    }

    public final double getProgress() {
        return progressBar.getProgress();
    }

    public final void setProgress(double progress) {
        progressBar.setProgress(progress);
    }

    public final DoubleProperty progressProperty() {
        return progressBar.progressProperty();
    }
}
