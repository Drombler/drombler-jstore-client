package org.drombler.jstore.client.branding.impl.layout;

import javafx.scene.layout.BorderPane;

public interface ContentPaneProvider {
    // TODO: replace BorderPane with a custom layout pane similar to DockablePane

    BorderPane getContentPane();
}
