package org.drombler.jstore.client.data;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.drombler.commons.fx.beans.binding.CollectionBindings;
import org.drombler.fx.startup.main.DromblerFXConfiguration;
import org.drombler.jstore.client.integration.store.StoreRestClientRegistryProvider;
import org.drombler.jstore.client.model.ObjectMapperProvider;
import org.drombler.jstore.client.model.json.DeviceConfigurations;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;


//@DocumentHandler(mimeType = DEVICE_CONFIGURATIONS_MIME_TYPE)
@Component
public class DeviceConfigurationsFileHandler implements DeviceHandlerListProvider {
    public static final String DEVICE_CONFIGURATIONS_FILE_NAME = "deviceConfigurations.json";

    @Reference
    private DromblerFXConfiguration dromblerFXConfiguration;
    @Reference
    private ObjectMapperProvider objectMapperProvider;
    @Reference
    private StoreRestClientRegistryProvider storeRestClientRegistryProvider;
    @Reference
    private DataHandlerRegistryProvider dataHandlerRegistryProvider;

    private DeviceConfigurations deviceConfigurations;
    private final ObservableList<DeviceHandler> deviceHandlers = FXCollections.observableArrayList();
    private Path deviceConfigurationsFilePath;


    @Activate
    protected void activate(ComponentContext context) throws IOException {
        this.deviceConfigurationsFilePath = dromblerFXConfiguration.getUserConfigDirPath().resolve(DEVICE_CONFIGURATIONS_FILE_NAME);
        makeSureDeviceConfigurationsFileExists();
        this.deviceConfigurations = readContent();
        deviceHandlers.addAll(getInitialDeviceList());
        deviceHandlers.forEach(DeviceHandler::connect);
        CollectionBindings.bindContent(deviceConfigurations.getDeviceConfigurations(), deviceHandlers, DeviceHandler::getDeviceConfiguration);
        saveIfUpdatedDuringInitialization();
        deviceHandlers.addListener((ListChangeListener<DeviceHandler>) change -> {
            try {
                writeContent();
            } catch (IOException e) {
                // TODO: show error dilaog
                // TODO: log with SLF4J
                e.printStackTrace();
            }
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(dataHandlerRegistryProvider.getDataHandlerRegistry()::unregisterDataHandler);
                } else if (change.wasAdded()) {
                    change.getAddedSubList().forEach(dataHandlerRegistryProvider.getDataHandlerRegistry()::registerDataHandler);
                }
            }
        });
    }

    private void makeSureDeviceConfigurationsFileExists() throws IOException {
        if (!Files.exists(deviceConfigurationsFilePath)) {
            Files.copy(dromblerFXConfiguration.getInstallConfigDirPath().resolve(DEVICE_CONFIGURATIONS_FILE_NAME), deviceConfigurationsFilePath);
        }
    }

    private void saveIfUpdatedDuringInitialization() {
        boolean modified = deviceHandlers.stream().anyMatch(DeviceHandler::isModified);
        if (modified) {
            try {
                writeContent();
            } catch (IOException e) {
                // TODO: show error dilaog
                // TODO: log with SLF4J
                e.printStackTrace();
            }
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        deviceHandlers.clear();
        this.deviceConfigurations = null;
    }


    private List<DeviceHandler> getInitialDeviceList() {
        return this.deviceConfigurations.getDeviceConfigurations().stream()
                .map(deviceConfiguration -> {
                    DeviceHandler deviceHandler = new DeviceHandler(deviceConfiguration, objectMapperProvider.getObjectMapper(), storeRestClientRegistryProvider.getStoreRestClientRegistry());
                    dataHandlerRegistryProvider.getDataHandlerRegistry().registerDataHandler(deviceHandler);
                    return deviceHandler;
                })
                .collect(Collectors.toList());
    }

    private DeviceConfigurations readContent() throws IOException {
        return objectMapperProvider.getObjectMapper().readValue(deviceConfigurationsFilePath.toFile(), DeviceConfigurations.class);
    }


    private void writeContent() throws IOException {
        objectMapperProvider.getObjectMapper().writeValue(deviceConfigurationsFilePath.toFile(), deviceConfigurations);
    }

    @Override
    public ObservableList<DeviceHandler> getDeviceHandlers() {
        return deviceHandlers;
    }
}
