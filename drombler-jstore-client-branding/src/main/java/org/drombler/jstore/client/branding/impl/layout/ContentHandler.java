package org.drombler.jstore.client.branding.impl.layout;

import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.fx.core.application.ApplicationContentProvider;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class ContentHandler {
    @Reference
    private ContentPaneProvider contentPaneProvider;
    @Reference
    private ApplicationContentProvider applicationContentProvider;
    @Reference
    private ApplicationThreadExecutorProvider applicationThreadExecutorProvider;

    @Activate
    protected void activate(ComponentContext context) {
        applicationThreadExecutorProvider.getApplicationThreadExecutor().execute(()
                -> contentPaneProvider.getContentPane().setCenter(applicationContentProvider.getContentPane()));
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        applicationThreadExecutorProvider.getApplicationThreadExecutor().execute(() -> {
            contentPaneProvider.getContentPane().setCenter(null);
            contentPaneProvider = null;
        });
    }
}
