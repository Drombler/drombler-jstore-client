package org.drombler.jstore.client.vendor.impl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.jstore.client.vendor.VendorInfoHandler;

/**
 *
 * @author puce
 */
public class VendorPane extends GridPane {

    private final VendorInfoHandler vendorInfoHandler;


    @FXML
    private TextField vendorIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField registrationEmailAddressField;

    @FXML
    private TextField customerContactEmailAddressField;

    @FXML
    private ListView<String> namespacesListView;

    public VendorPane(VendorInfoHandler vendorInfoHandler) throws NoSuchMethodException {
        FXMLLoaders.loadRoot(this);
        this.vendorInfoHandler = vendorInfoHandler;


        vendorIdField.textProperty().bindBidirectional(vendorInfoHandler.vendorIdProperty());
        nameField.textProperty().bindBidirectional(name);
        registrationEmailAddressField.textProperty().bindBidirectional(registrationEmailAddress);
        customerContactEmailAddressField.textProperty().bindBidirectional(customerContactEmailAddress);
        namespacesListView.setItems(namespaces);

    }

    @FXML
    private void addNamespace(ActionEvent event) {

    }

    @FXML
    private void removeNamespace(ActionEvent event) {
        namespaces.removeAll(namespacesListView.getSelectionModel().getSelectedItems());
    }

}
