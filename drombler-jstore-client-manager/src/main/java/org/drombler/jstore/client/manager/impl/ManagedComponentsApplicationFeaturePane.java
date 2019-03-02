package org.drombler.jstore.client.manager.impl;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.drombler.commons.context.Context;
import org.drombler.commons.context.ContextWrapper;
import org.drombler.commons.context.Contexts;
import org.drombler.commons.context.LocalContextProvider;
import org.drombler.commons.context.ProxyContext;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.jstore.client.branding.ApplicationFeature;
import org.drombler.jstore.client.branding.DeviceFeatureToggleButton;
import org.drombler.jstore.client.branding.NavigationBar;
import static org.drombler.jstore.client.manager.impl.ManagedComponentsApplicationFeatureToggleAction.ID;

/**
 * @author puce
 */
@ApplicationFeature(actionId = ID, position = 10)
public class ManagedComponentsApplicationFeaturePane extends BorderPane implements LocalContextProvider {

    private final ProxyContext activeSelectionProxyContext = new ProxyContext();
    private final Context activeSelectionProxyContextWrapper = new ContextWrapper(activeSelectionProxyContext);

    @FXML
    private NavigationBar navigationBar;

    public ManagedComponentsApplicationFeaturePane() {
        FXMLLoaders.loadRoot(this);

        navigationBar.selectedDeviceToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && oldValue instanceof LocalContextProvider) {
                Context localContext = Contexts.getLocalContext(oldValue);
                activeSelectionProxyContext.removeContext(localContext);
            }
            if (newValue != null && newValue instanceof LocalContextProvider) {
                Context localContext = Contexts.getLocalContext(newValue);
                activeSelectionProxyContext.addContext(localContext);
            }
        });

        navigationBar.selectedDeviceFeatureToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                final Node oldDeviceFeatureContent = ((DeviceFeatureToggleButton) oldValue).getData().getDeviceFeatureContent();
                if (oldDeviceFeatureContent instanceof LocalContextProvider) {
                    Context localContext = Contexts.getLocalContext(oldDeviceFeatureContent);
                    activeSelectionProxyContext.removeContext(localContext);
                }
            }

            if (newValue != null) {
                final Node newDeviceFeatureContent = ((DeviceFeatureToggleButton) newValue).getData().getDeviceFeatureContent();
                if (newDeviceFeatureContent instanceof LocalContextProvider) {
                    Context localContext = Contexts.getLocalContext(newDeviceFeatureContent);
                    activeSelectionProxyContext.addContext(localContext);
                }
                setCenter(newDeviceFeatureContent);
            } else {
                setCenter(null);
            }
        });
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    @Override
    public Context getLocalContext() {
        return activeSelectionProxyContextWrapper;
    }
}
