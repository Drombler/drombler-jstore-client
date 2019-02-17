package org.drombler.jstore.client.branding.impl;

import java.util.Objects;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.drombler.commons.action.fx.ButtonUtils;
import org.drombler.commons.action.fx.FXToggleAction;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.commons.fx.scene.control.XToggleButton;
import org.drombler.jstore.client.branding.ApplicationFeatureDescriptor;
import org.softsmithy.lib.util.Positionables;

/**
 *
 * @author puce
 */
public class ApplicationFeatureBar extends GridPane {

    private ObservableList<ApplicationFeatureDescriptor<?>> applicationFeatures = FXCollections.observableArrayList();

    private final ReadOnlySelectedApplicationFeatureProperty selectedApplicationFeature = new ReadOnlySelectedApplicationFeatureProperty();

    @FXML
    private HBox applicationFeatureHBox;

    @FXML
    private LoginButtonPane loginButtonPane;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    public ApplicationFeatureBar() {
        FXMLLoaders.loadRoot(this);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null){
                int index = applicationFeatureHBox.getChildren().indexOf(newValue); // TODO: better way
                selectedApplicationFeature.set(applicationFeatures.get(index));
            } else {
                selectedApplicationFeature.set(null);
            }
        });
    }

    public final ApplicationFeatureDescriptor<?> getSelectedApplicationFeature() {
        return selectedApplicationFeatureProperty().get();
    }

    public ReadOnlyObjectProperty<ApplicationFeatureDescriptor<?>> selectedApplicationFeatureProperty() {
        return selectedApplicationFeature;
    }

    public <T extends Node> void addApplicationFeatures(ApplicationFeatureDescriptor<T> applicationFeatureDescriptor, FXToggleAction toggleAction) {
        int insertionPoint = Positionables.getInsertionPoint(applicationFeatures,applicationFeatureDescriptor);
        applicationFeatures.add(insertionPoint, applicationFeatureDescriptor);
        XToggleButton toggleButton = new XToggleButton();
        ButtonUtils.configureToolbarToggleButton(toggleButton, toggleAction, 24);
        toggleGroup.getToggles().add(toggleButton);
        applicationFeatureHBox.getChildren().add(insertionPoint, toggleButton);

    }

    private class ReadOnlySelectedApplicationFeatureProperty extends ReadOnlyObjectPropertyBase<ApplicationFeatureDescriptor<?>> {
        private ApplicationFeatureDescriptor<?> applicationFeatureDescriptor = null;

        @Override
        public ApplicationFeatureDescriptor<?> get() {
            return applicationFeatureDescriptor;
        }

        private void set(ApplicationFeatureDescriptor<?> newValue) {
            if (!Objects.equals(applicationFeatureDescriptor, newValue)) {
                applicationFeatureDescriptor = newValue;
                fireValueChangedEvent();
            }
        }

        @Override
        public Object getBean() {
            return ApplicationFeatureBar.this;
        }

        @Override
        public String getName() {
            return "selectedApplicationFeature";
        }


    }
}
