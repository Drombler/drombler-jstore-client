package org.drombler.jstore.client.vendor.impl;

import org.drombler.acp.core.action.ToggleAction;
import org.drombler.commons.action.AbstractToggleActionListener;
import static org.drombler.jstore.client.vendor.impl.VendorApplicationFeatureToggleAction.ID;

/**
 *
 * @author puce
 */

@ToggleAction(id = ID, category = "jstore", displayName = "Vendor")
public class VendorApplicationFeatureToggleAction extends AbstractToggleActionListener<Object> {
    public static final String ID = "VendorApplicationFeatureToggleAction";

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        // nothing to do
    }


}
