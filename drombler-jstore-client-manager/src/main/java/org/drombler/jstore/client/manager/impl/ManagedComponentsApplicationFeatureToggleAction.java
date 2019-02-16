package org.drombler.jstore.client.manager.impl;

import org.drombler.acp.core.action.ToggleAction;
import org.drombler.commons.action.AbstractToggleActionListener;
import static org.drombler.jstore.client.manager.impl.ManagedComponentsApplicationFeatureToggleAction.ID;

/**
 *
 * @author puce
 */

@ToggleAction(id = ID, category = "jstore", displayName = "MC")
public class ManagedComponentsApplicationFeatureToggleAction extends AbstractToggleActionListener<Object> {
    public static final String ID = "ManagedComponentsApplicationFeatureToggleAction";

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
