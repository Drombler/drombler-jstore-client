package org.drombler.jstore.client.manager.impl;

import java.util.List;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import org.drombler.commons.context.ActiveContextSensitive;
import org.drombler.commons.context.Context;
import org.drombler.commons.context.ContextEvent;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.commons.fx.scene.control.RenderedListCellFactory;
import org.drombler.jstore.client.branding.DeviceFeature;
import org.drombler.jstore.client.data.DeviceHandler;
import org.drombler.jstore.client.integration.store.StoreRestClient;
import org.drombler.jstore.protocol.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DeviceFeature(displayName = "%displayName", position = 50, selected = true)
public class UpdateApplicationDeviceFeatureContentPane extends BorderPane implements ActiveContextSensitive {

    private final static Logger LOGGER = LoggerFactory.getLogger(UpdateApplicationDeviceFeatureContentPane.class);
    private Context activeContext;
    private DeviceHandler device;

    @FXML
    private ListView<UpgradableApplication> updateListView;

    public UpdateApplicationDeviceFeatureContentPane() {
        FXMLLoaders.loadRoot(this);
        updateListView.setCellFactory(new RenderedListCellFactory<>(new UpgradableApplicationRenderer()));
    }

    @Override
    public void setActiveContext(Context activeContext) {
        this.activeContext = activeContext;
        this.activeContext.addContextListener(DeviceHandler.class, this::contextChanged);
        contextChanged(new ContextEvent<>(activeContext, DeviceHandler.class));
    }

    private void contextChanged(ContextEvent<DeviceHandler> event) {
        DeviceHandler newDevice = activeContext.find(event.getType());
        if (!Objects.equals(device, newDevice)) {
            if (device != null) {
                resetUpdateListView();
            }
            device = newDevice;
            if (device != null) {
                LOGGER.debug("New device found: {}", device);
                refreshUpdateListView();
            }
        }

    }

    private void refreshUpdateListView() {
        List<SelectedApplication> selectedApplications = device.getJStoreClientAgentSocketClient().getSelectedApplications();
        LOGGER.debug("SelectedApplications: {}", selectedApplications);
        List<StoreRestClient> storeRestClients = device.getStoreRestClients();
        storeRestClients.forEach(storeRestClient -> {
            ApplicationVersionSearchRequest request = new ApplicationVersionSearchRequest();
            request.setSelectedApplications(selectedApplications);
            request.setSystemInfo(device.getJStoreClientAgentSocketClient().getSystemInfo());
            try {
                // TODO: split per store
                ApplicationVersionSearchResponse response = storeRestClient.startApplicationVersionSearch(request);
                List<UpgradableApplication> upgradableApplications = response.getUpgradableApplications();
                LOGGER.debug("UpgradableApplications: {}", upgradableApplications);
                updateListView.getItems().addAll(upgradableApplications);
            } catch (RuntimeException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        });
    }

    private void resetUpdateListView() {
        updateListView.getItems().clear();
    }
}
