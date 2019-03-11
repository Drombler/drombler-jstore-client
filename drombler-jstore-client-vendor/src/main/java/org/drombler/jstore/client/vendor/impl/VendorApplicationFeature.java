package org.drombler.jstore.client.vendor.impl;

import org.drombler.jstore.client.store.StoreManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 *
 * @author puce
 */
@Component
public class VendorApplicationFeature {

    @Reference
    private StoreManager storeManager;
}
