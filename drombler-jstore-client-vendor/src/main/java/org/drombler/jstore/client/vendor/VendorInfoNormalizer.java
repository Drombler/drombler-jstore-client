package org.drombler.jstore.client.vendor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.ListIterator;
import org.apache.commons.lang3.StringUtils;
import org.drombler.jstore.protocol.json.VendorInfo;

/**
 *
 * @author puce
 */


public class VendorInfoNormalizer {


  public  void normalize(VendorInfo vendorInfo) {
        vendorInfo.setVendorId(StringUtils.trimToNull(vendorInfo.getVendorId()));
        vendorInfo.setName(StringUtils.trimToNull(vendorInfo.getName()));
        vendorInfo.setRegistrationEmailAddress(StringUtils.trimToNull(vendorInfo.getRegistrationEmailAddress()));
        vendorInfo.setCustomerContactEmailAddress(StringUtils.trimToNull(vendorInfo.getCustomerContactEmailAddress()));
        for (ListIterator<String> iterator = vendorInfo.getNamespaces().listIterator(); iterator.hasNext();) {
            String namespace = StringUtils.trimToNull(iterator.next());
            if (namespace != null) {
                iterator.set(namespace);
            } else {
                iterator.remove();
            }
        }
        vendorInfo.setNamespaces(new ArrayList<>(new LinkedHashSet<>(vendorInfo.getNamespaces())));
    }
    
}
