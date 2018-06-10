package org.drombler.jstore.client.manager.impl;

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
import org.drombler.jstore.protocol.json.ApplicationId;
import org.drombler.jstore.protocol.json.ApplicationVersionInfo;
import org.drombler.jstore.protocol.json.ApplicationVersionSearchRequest;
import org.drombler.jstore.protocol.json.ApplicationVersionSearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

@DeviceFeature(displayName = "%displayName", position = 50, selected = true)
public class UpdateFeatureContentPane extends BorderPane implements ActiveContextSensitive {
    private final static Logger LOGGER = LoggerFactory.getLogger(UpdateFeatureContentPane.class);
    private Context activeContext;
    private DeviceHandler device;

    @FXML
    private ListView<ApplicationVersionInfo> updateListView;

    public UpdateFeatureContentPane() {
        FXMLLoaders.loadRoot(this);
        updateListView.setCellFactory(new RenderedListCellFactory<>(new ApplicationVersionInfoRenderer()));
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
        List<ApplicationId> applicationIds = device.getJStoreClientAgentSocketClient().getSelectedApplications();
        LOGGER.debug("ApplicationIds: {}", applicationIds);
        List<StoreRestClient> storeRestClients = device.getStoreRestClients();
        storeRestClients.forEach(storeRestClient -> {
            ApplicationVersionSearchRequest request = new ApplicationVersionSearchRequest();
            request.setApplicationIds(applicationIds);
            // TODO: split per store
            ApplicationVersionSearchResponse response = storeRestClient.startApplicationVersionSearch(request);
            List<ApplicationVersionInfo> applicationVersionInfos = response.getApplicationVersionInfos();
            LOGGER.debug("ApplicationVersionInfos: {}", applicationVersionInfos);
            updateListView.getItems().addAll(applicationVersionInfos);
        });
    }

    private void resetUpdateListView() {
        updateListView.getItems().clear();
    }
}
