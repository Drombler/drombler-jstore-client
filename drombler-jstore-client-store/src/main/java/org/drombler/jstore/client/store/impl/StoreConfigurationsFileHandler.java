package org.drombler.jstore.client.store.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.drombler.commons.fx.beans.binding.CollectionBindings;
import org.drombler.fx.startup.main.DromblerFXConfiguration;
import org.drombler.jstore.client.model.ObjectMapperProvider;
import org.drombler.jstore.client.store.StoreHandler;
import org.drombler.jstore.client.store.StoreHandlerListProvider;
import org.drombler.jstore.client.store.StoreHandlerRegistryProvider;
import org.drombler.jstore.protocol.json.StoreConfigurations;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

//@DocumentHandler(mimeType = STORE_CONFIGURATIONS_MIME_TYPE)
@Component
public class StoreConfigurationsFileHandler implements StoreHandlerListProvider {

    public static final String STORE_CONFIGURATIONS_FILE_NAME = "stores.json";

    @Reference
    private DromblerFXConfiguration dromblerFXConfiguration;
    @Reference
    private ObjectMapperProvider objectMapperProvider;
    @Reference
    private StoreHandlerRegistryProvider storeHandlerRegistryProvider;
    @Reference
    private DataHandlerRegistryProvider dataHandlerRegistryProvider;

    private StoreConfigurations storeConfigurations;
    private final ObservableList<StoreHandler> storeHandlers = FXCollections.observableArrayList();
    private Path storeConfigurationsFilePath;

    @Activate
    protected void activate(ComponentContext context) throws IOException {
        this.storeConfigurationsFilePath = dromblerFXConfiguration.getUserConfigDirPath().resolve(STORE_CONFIGURATIONS_FILE_NAME);
        makeSureStoreConfigurationsFileExists();
        this.storeConfigurations = readContent();
        storeHandlers.addAll(getInitialStoreList());
        storeHandlers.forEach(storeHandler -> storeHandlerRegistryProvider.getStoreHandlerRegistry().registerStoreHandler(storeHandler));
        CollectionBindings.bindContent(storeConfigurations.getStores(), storeHandlers, StoreHandler::getStore);
        saveIfUpdatedDuringInitialization();
        storeHandlers.addListener((ListChangeListener<StoreHandler>) change -> {
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
                } else {
                    if (change.wasAdded()) {
                        change.getAddedSubList().forEach(dataHandlerRegistryProvider.getDataHandlerRegistry()::registerDataHandler);
                    }
                }
            }
        });
    }

    private void makeSureStoreConfigurationsFileExists() throws IOException {
        if (!Files.exists(storeConfigurationsFilePath)) {
            Files.copy(dromblerFXConfiguration.getInstallConfigDirPath().resolve(STORE_CONFIGURATIONS_FILE_NAME), storeConfigurationsFilePath);
        }
    }

    private void saveIfUpdatedDuringInitialization() {
        boolean modified = storeHandlers.stream().anyMatch(StoreHandler::isModified);
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
        storeHandlers.clear();
        this.storeConfigurations = null;
    }

    private List<StoreHandler> getInitialStoreList() {
        return this.storeConfigurations.getStores().stream()
                .map(store -> {
            StoreHandler storeHandler = new StoreHandler(store);
                    dataHandlerRegistryProvider.getDataHandlerRegistry().registerDataHandler(storeHandler);
                    return storeHandler;
                })
                .collect(Collectors.toList());
    }

    private StoreConfigurations readContent() throws IOException {
        return objectMapperProvider.getObjectMapper().readValue(storeConfigurationsFilePath.toFile(), StoreConfigurations.class);
    }

    private void writeContent() throws IOException {
        objectMapperProvider.getObjectMapper().writeValue(storeConfigurationsFilePath.toFile(), storeConfigurations);
    }

    @Override
    public ObservableList<StoreHandler> getStoreHandlers() {
        return storeHandlers;
    }
}
