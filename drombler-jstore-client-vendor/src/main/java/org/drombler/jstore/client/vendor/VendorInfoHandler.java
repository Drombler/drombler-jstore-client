package org.drombler.jstore.client.vendor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.ListIterator;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.data.AbstractDataHandler;
import org.drombler.acp.core.data.BusinessObjectHandler;
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

    private boolean newVendorInfo;

    public VendorInfoHandler(VendorInfo vendorInfo, StoreRestClient storeRestClient) {
        this.vendorInfo = vendorInfo;
        this.storeRestClient = storeRestClient;
        this.newVendorInfo = vendorInfo.getVendorId() == null;
    }

    @Override
    public String getTitle() {
        return vendorInfo.getName();
    }

    @Override
    public String getTooltipText() {
        return vendorInfo.getVendorId();
    }

    @Override
    public String getUniqueKey() {
        return newVendorInfo ? null : vendorInfo.getVendorId();
    }

    public void saveNew() {
        normalize();
        CreateVendorRequest request = new CreateVendorRequest();
        request.setVendorInfo(vendorInfo);
        storeRestClient.createVendor(request);
        newVendorInfo = false;
    }

    public VendorInfo getVendorInfo() {
        return vendorInfo;
    }
    
    private void normalize(){
        vendorInfo.setVendorId(StringUtils.trimToNull(vendorInfo.getVendorId()));
        vendorInfo.setName(StringUtils.trimToNull(vendorInfo.getName()));
        vendorInfo.setRegistrationEmailAddress(StringUtils.trimToNull(vendorInfo.getRegistrationEmailAddress()));
        vendorInfo.setCustomerContactEmailAddress(StringUtils.trimToNull(vendorInfo.getCustomerContactEmailAddress()));
        
        for (ListIterator<String> iterator = vendorInfo.getNamespaces().listIterator(); iterator.hasNext();){
            String namespace = StringUtils.trimToNull(iterator.next());
            if (namespace != null){
                iterator.set(namespace);
            } else {
                iterator.remove();
            }
        }
        
        vendorInfo.setNamespaces(new ArrayList<>(new LinkedHashSet<>(vendorInfo.getNamespaces())));
    }

}
