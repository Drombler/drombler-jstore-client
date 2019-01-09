package org.drombler.jstore.client.vendor.impl;

import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private final JavaBeanStringProperty vendorId;
    private final JavaBeanStringProperty name;
    private final JavaBeanStringProperty registrationEmailAddress;
    private final JavaBeanStringProperty customerContactEmailAddress;
    private final ObservableList<String> namespaces;

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
        JavaBeanStringPropertyBuilder builder = JavaBeanStringPropertyBuilder.create().bean(vendorInfoHandler.getVendorInfo());
        this.vendorId = builder.name("vendorId").build();
        this.name = builder.name("name").build();
        this.registrationEmailAddress = builder.name("registrationEmailAddress").build();
        this.customerContactEmailAddress = builder.name("customerContactEmailAddress").build();
        this.namespaces = FXCollections.observableList(vendorInfoHandler.getVendorInfo().getNamespaces());

        vendorIdField.textProperty().bindBidirectional(vendorId);
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
