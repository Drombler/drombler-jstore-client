package org.drombler.jstore.client.vendor.impl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.jstore.client.branding.ApplicationFeature;
import org.drombler.jstore.client.vendor.VendorInfoHandler;
import static org.drombler.jstore.client.vendor.impl.VendorApplicationFeatureToggleAction.ID;
import org.drombler.jstore.protocol.json.VendorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
@ApplicationFeature(actionId = ID, position = 20)
public class VendorManagerPane extends BorderPane {

    private final static Logger LOGGER = LoggerFactory.getLogger(VendorManagerPane.class);

    @FXML
    private BorderPane vendorContentPane;

    @FXML
    private Button createButton;

    @FXML
    private Button updateButton;

    private final ObjectProperty<VendorInfoHandler> vendorInfoHandler = new SimpleObjectProperty<>(this, "vendorInfoHandler");

    public VendorManagerPane() {
        FXMLLoaders.loadRoot(this);

        vendorContentPane.setCenter(null);
        createButton.setVisible(false);
        updateButton.setVisible(false);

        createButton.setOnAction(event -> getVendorInfoHandler().saveNew());
        updateButton.setOnAction(event -> getVendorInfoHandler().save());

        vendorInfoHandler.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    VendorPane vendorPane = new VendorPane(newValue);
                    vendorContentPane.setCenter(vendorPane);
                    createButton.setVisible(newValue.getUniqueKey() == null);
                    updateButton.setVisible(newValue.getUniqueKey() != null);
                } catch (NoSuchMethodException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            } else {
                vendorContentPane.setCenter(null);
                createButton.setVisible(false);
                updateButton.setVisible(false);
            }
        });

    }

    public void addVendor(ActionEvent event) throws NoSuchMethodException {
        VendorInfoHandler vendorInfoHandler = new VendorInfoHandler(new VendorInfo(), null);
        setVendorInfoHandler(vendorInfoHandler);
    }

    public VendorInfoHandler getVendorInfoHandler() {
        return vendorInfoHandlerProperty().get();
    }

    public final void setVendorInfoHandler(VendorInfoHandler vendorInfoHandler) {
        vendorInfoHandlerProperty().set(vendorInfoHandler);
    }

    public ObjectProperty<VendorInfoHandler> vendorInfoHandlerProperty() {
        return vendorInfoHandler;
    }
}
