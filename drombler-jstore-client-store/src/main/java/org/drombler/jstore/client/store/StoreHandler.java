package org.drombler.jstore.client.store;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import java.util.UUID;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.data.AbstractDataHandler;
import org.drombler.acp.core.data.BusinessObjectHandler;
import org.drombler.jstore.client.integration.store.StoreRestClient;
import org.drombler.jstore.protocol.json.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@BusinessObjectHandler
public class StoreHandler extends AbstractDataHandler<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreHandler.class);

    private final Store store;
    private final StoreRestClient storeRestClient;

    private final BooleanProperty modified = new SimpleBooleanProperty(this, "modified", false);

    private final Feign.Builder feignBuilder = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            //                .client(new OkHttpClient())
            .logger(new Slf4jLogger());

    public StoreHandler(Store store) {
        this.store = store;
        this.storeRestClient = feignBuilder.target(StoreRestClient.class, store.getEndpoint());

        if (StringUtils.isBlank(store.getId())) {
            store.setId(UUID.randomUUID().toString());
            setModified(true);
        }
    }

    @Override
    public String getTitle() {
        return store.getDisplayName();
    }

    @Override
    public String getTooltipText() {
        return store.toString();
    }

    @Override
    public String getUniqueKey() {
        return store.getEndpoint(); // use the endpoint as unique key rather than the id, since the endpoint is unique accross devices
    }

    public final boolean isModified() {
        return modifiedProperty().get();
    }

    public final void setModified(boolean modified) {
        modifiedProperty().set(modified);
    }

    public BooleanProperty modifiedProperty() {
        return modified;
    }

    public Store getStore() {
        return store;
    }

    /**
     * @return the storeRestClient
     */
    public StoreRestClient getStoreRestClient() {
        return storeRestClient;
    }

}
