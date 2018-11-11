package org.drombler.jstore.client.branding.impl.layout;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.drombler.acp.core.action.spi.ApplicationToolBarContainerProvider;
import org.drombler.acp.core.action.spi.MenuBarMenuContainerProvider;
import org.drombler.acp.core.action.spi.MenuMenuItemContainerFactory;
import org.drombler.acp.core.action.spi.SeparatorMenuItemFactory;
import org.drombler.acp.core.status.spi.StatusBarElementContainer;
import org.drombler.acp.core.status.spi.StatusBarElementContainerProvider;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.commons.fx.scene.control.StatusBar;
import org.drombler.fx.core.action.FXMenuBarMenuContainer;
import org.drombler.fx.core.status.FXStatusBarElementContainer;
import org.drombler.jstore.client.branding.impl.NavigationBar;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JStoreDesktopApplicationPane extends GridPane implements MenuBarMenuContainerProvider<MenuItem, Menu>,
        ContentPaneProvider,   StatusBarElementContainerProvider, Initializable {

    private final FXMenuBarMenuContainer menuBarMenuContainer;
    private final FXStatusBarElementContainer statusBarElementContainer;

    @FXML
    private MenuBar menuBar;

    @FXML
    private BorderPane contentPane;

    @FXML
    private StatusBar statusBar;

    public JStoreDesktopApplicationPane(MenuMenuItemContainerFactory<MenuItem, Menu> menuMenuItemContainerFactory, SeparatorMenuItemFactory<SeparatorMenuItem> separatorMenuItemFactory) throws
            IOException {
        load();
        menuBarMenuContainer = new FXMenuBarMenuContainer(menuBar, menuMenuItemContainerFactory, separatorMenuItemFactory);
        statusBarElementContainer = new FXStatusBarElementContainer(statusBar);
    }

    private void load() throws IOException {
        FXMLLoaders.loadRoot(this);
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuBar.setUseSystemMenuBar(true);
    }

    @Override
    public FXMenuBarMenuContainer getMenuBarMenuContainer() {
        return menuBarMenuContainer;
    }

    @Override
    public BorderPane getContentPane() {
        return contentPane;
    }

    @Override
    public StatusBarElementContainer getStatusBarElementContainer() {
        return statusBarElementContainer;
    }
}