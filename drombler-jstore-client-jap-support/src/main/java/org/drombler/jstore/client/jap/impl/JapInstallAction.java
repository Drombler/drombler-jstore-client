

package org.drombler.jstore.client.jap.impl;

import java.nio.file.Path;
import javafx.event.ActionEvent;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.drombler.commons.action.fx.AbstractFXAction;
import org.drombler.commons.client.dialog.FileChooserProvider;
import org.drombler.commons.data.Openable;
import org.drombler.commons.fx.concurrent.FXConsumer;
import org.osgi.util.tracker.ServiceTracker;


@Action(id = "jap-install", category = "jstore", displayName = "%jap-install.displayName", accelerator = "Shortcut+I")
@MenuEntry(path = "File", position = 10)
public class JapInstallAction extends AbstractFXAction implements AutoCloseable {

    private final ServiceTracker<DataHandlerRegistryProvider, DataHandlerRegistryProvider> dataHandlerRegistryServiceTracker;
    private DataHandlerRegistryProvider dataHandlerRegistryProvider;
    private FileChooserProvider fileChooserProvider;


    public JapInstallAction() {
        this.dataHandlerRegistryServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DataHandlerRegistryProvider.class, new FXConsumer<>(this::setDataHandlerRegistryProvider));
        this.dataHandlerRegistryServiceTracker.open(true);
        setEnabled(isInitialized());
    }

    private boolean isInitialized() {
        return dataHandlerRegistryProvider != null;
    }

    @Override
    public void handle(ActionEvent event) {
        Path jpaFilePath = fileChooserProvider.showOpenDialog();
        JapHandler japHandler = new JapHandler(jpaFilePath);
        dataHandlerRegistryProvider.getDataHandlerRegistry().registerDataHandler(japHandler);
        Openable openable = japHandler.getLocalContext().find(Openable.class);
        if (openable != null) {
            openable.open();
        }
    }

    @Override
    public void close() {
        dataHandlerRegistryServiceTracker.close();
    }

    /**
     * @return the dataHandlerRegistryProvider
     */
    public DataHandlerRegistryProvider getDataHandlerRegistryProvider() {
        return dataHandlerRegistryProvider;
    }

    /**
     * @param dataHandlerRegistryProvider the dataHandlerRegistryProvider to set
     */
    public void setDataHandlerRegistryProvider(DataHandlerRegistryProvider dataHandlerRegistryProvider) {
        this.dataHandlerRegistryProvider = dataHandlerRegistryProvider;
        setEnabled(isInitialized());
    }

}
