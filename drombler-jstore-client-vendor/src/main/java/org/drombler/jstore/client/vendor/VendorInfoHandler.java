package org.drombler.jstore.client.vendor;

import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.drombler.acp.core.data.AbstractDataHandler;
import org.drombler.acp.core.data.BusinessObjectHandler;
import org.drombler.commons.data.DataHandler;
import org.drombler.jstore.client.integration.store.StoreRestClient;
import org.drombler.jstore.protocol.json.CreateVendorRequest;
import org.drombler.jstore.protocol.json.VendorInfo;

/**
 *
 * @author puce
 */
@BusinessObjectHandler
public class VendorInfoHandler extends AbstractDataHandler<String> {

    private final VendorInfo vendorInfo;
    private final StoreRestClient storeRestClient;

    private final JavaBeanStringProperty vendorId;
    private final JavaBeanStringProperty name;
    private final JavaBeanStringProperty registrationEmailAddress;
    private final JavaBeanStringProperty customerContactEmailAddress;
    private final ObservableList<String> namespaces;

    private final StringProperty tooltipText = new SimpleStringProperty(this, TOOLTIP_TEXT_PROPERTY_NAME);
    private final StringProperty title = new SimpleStringProperty(this, TITLE_PROPERTY_NAME);

    private boolean newVendorInfo;

    public VendorInfoHandler(VendorInfo vendorInfo, StoreRestClient storeRestClient) {
        try {
            this.vendorInfo = vendorInfo;
            this.storeRestClient = storeRestClient;
            this.newVendorInfo = vendorInfo.getVendorId() == null;

            JavaBeanStringPropertyBuilder builder = JavaBeanStringPropertyBuilder.create().bean(vendorInfo);
            this.vendorId = builder.name("vendorId").build();
            this.name = builder.name("name").build();
            this.registrationEmailAddress = builder.name("registrationEmailAddress").build();
            this.customerContactEmailAddress = builder.name("customerContactEmailAddress").build();

            this.namespaces = FXCollections.observableList(vendorInfo.getNamespaces());

            this.vendorId.addListener((observable, oldValue, newValue)
                    -> getPropertyChangeSupport().firePropertyChange(vendorId.getName(), oldValue, newValue));

            this.tooltipText.bind(vendorId);
            this.tooltipText.addListener((observable, oldValue, newValue)
                    -> getPropertyChangeSupport().firePropertyChange(tooltipText.getName(), oldValue, newValue));

            this.title.bind(name);
            this.title.addListener((observable, oldValue, newValue)
                    -> getPropertyChangeSupport().firePropertyChange(title.getName(), oldValue, newValue));
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public String getTitle() {
        return title.get();
    }

    @Override
    public String getTooltipText() {
        return tooltipText.get();
    }

    @Override
    public String getUniqueKey() {
        return newVendorInfo ? null : vendorInfo.getVendorId();
    }

    public final String getVendorId() {
        return vendorIdProperty().get();
    }

    public final void setVendorId(String vendorId) {
        vendorIdProperty().set(vendorId);
    }

    public JavaBeanStringProperty vendorIdProperty() {
        return vendorId;
    }

    public void saveNew() {
        VendorInfoNormalizer vendorInfoNormalizer = new VendorInfoNormalizer();
        vendorInfoNormalizer.normalize(vendorInfo); // TODO: property change events??!!
        
        CreateVendorRequest request = new CreateVendorRequest();
        request.setVendorInfo(vendorInfo);
        storeRestClient.createVendor(request);
        newVendorInfo = false;
        getPropertyChangeSupport().firePropertyChange(DataHandler.UNIQUE_KEY_PROPERTY_NAME, null, getUniqueKey());
    }

    public static List<VendorInfoHandler> loadAdministratableVendorInfos(StoreRestClient storeRestClient) {
        return Arrays.asList(new VendorInfoHandler(null, storeRestClient));
    }

    //    public VendorInfo getVendorInfo() {
    //        return vendorInfo;
    //    }
}
