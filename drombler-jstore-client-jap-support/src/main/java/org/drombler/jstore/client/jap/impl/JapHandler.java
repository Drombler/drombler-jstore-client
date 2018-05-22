

package org.drombler.jstore.client.jap.impl;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.drombler.acp.core.data.AbstractDocumentHandler;
import org.drombler.acp.core.data.DocumentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

@DocumentHandler(mimeType = JapUtils.MIME_TYPE, icon = "jap.png")
public class JapHandler extends AbstractDocumentHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JapHandler.class);
    private final StringProperty text = new SimpleStringProperty(this, "text");

    public JapHandler(Path path) {
        super(JapUtils.FILE_EXTENSION, path);
        markDirty();
    }

    public final String getText() {
        return textProperty().get();
    }

    public final void setText(String text) {
        textProperty().set(text);
    }

    public StringProperty textProperty() {
        if (isDirty()) {
            markClean();
            loadContent();
        }
        return text;
    }

    public void loadContent() {
        if (getPath() != null) {
            try {
                setText(String.join("\n", Files.readAllLines(getPath())));
            } catch (IOException ex) {
                LOG.error("Could not read from file: " + getPath(), ex);
            }
        } else {
            LOG.error("No file specified!");
        }
    }

    @Override
    protected void writeContent() throws IOException {
        Files.write(getPath(), Collections.singletonList(getText()));
    }

}
