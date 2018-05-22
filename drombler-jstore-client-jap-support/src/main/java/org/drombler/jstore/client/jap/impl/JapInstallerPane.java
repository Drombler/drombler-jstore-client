

package org.drombler.jstore.client.jap.impl;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import org.drombler.acp.core.docking.EditorDocking;
import org.drombler.commons.action.command.Savable;
import org.drombler.commons.context.Context;
import org.drombler.commons.context.LocalContextProvider;
import org.drombler.commons.context.SimpleContext;
import org.drombler.commons.context.SimpleContextContent;
import org.drombler.commons.docking.DockableDataSensitive;
import org.drombler.commons.docking.fx.FXDockableData;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.fx.core.docking.FXDockableDataUtils;

/**
 * @author puce
 */
@EditorDocking(contentType = JapHandler.class)
public class JapInstallerPane extends BorderPane implements DockableDataSensitive<FXDockableData>, LocalContextProvider {

    private final SimpleContextContent contextContent = new SimpleContextContent();
    private final Context localContext = new SimpleContext(contextContent);
    private final JapHandler japHandler;
    private FXDockableData dockableData;
    private Savable fooSavable;

    @FXML
    private TextArea textArea;

    public JapInstallerPane(JapHandler japHandler) {
        this.japHandler = japHandler;
        FXMLLoaders.loadRoot(this);
        textArea.textProperty().bindBidirectional(japHandler.textProperty());
        japHandler.textProperty().addListener((observable, oldValue, newValue) -> contentModified());
    }

    @Override
    public Context getLocalContext() {
        return localContext;
    }

    @Override
    public void setDockableData(FXDockableData dockableData) {
        this.dockableData = dockableData;
        FXDockableDataUtils.configureDockableData(this.dockableData, japHandler);
        this.fooSavable = FXDockableDataUtils.createDocumentSavable(japHandler, dockableData, this::cleanupAfterSave);
        if (japHandler.getUniqueKey() == null) {
            contentModified();
        }
    }

    private void contentModified() {
        if (localContext.find(Savable.class) == null) {
            contextContent.add(fooSavable);
        }
    }

    private void cleanupAfterSave(Savable savable) {
        contextContent.remove(savable);
    }

}
