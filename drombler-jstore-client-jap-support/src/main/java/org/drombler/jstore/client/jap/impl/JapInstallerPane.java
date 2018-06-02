

package org.drombler.jstore.client.jap.impl;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import org.drombler.commons.fx.fxml.FXMLLoaders;

/**
 * @author puce
 */
public class JapInstallerPane extends BorderPane {


    private final JapHandler japHandler;

    @FXML
    private ListView<Object> listView;

    public JapInstallerPane(JapHandler japHandler) {
        this.japHandler = japHandler;
        FXMLLoaders.loadRoot(this);
    }


}
