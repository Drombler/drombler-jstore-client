package org.drombler.jstore.client.integration.client.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.drombler.jstore.client.protocol.json.Store;
import org.drombler.jstore.protocol.json.ApplicationId;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class JStoreClientAgentSocketClient implements AutoCloseable {
    private Socket socket;
    private OutputStreamWriter writer;
    private final ObjectMapper objectMapper;

    public JStoreClientAgentSocketClient(String host, int port, ObjectMapper objectMapper) throws IOException {
        try {

            this.socket = new Socket(host, port);
            this.writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        } catch (IOException | IllegalArgumentException ex) {
// TODO: remove this try-catch again
        }
        this.objectMapper = objectMapper;
    }


    // ping()

    // getDeviceInfo()

    public List<Store> getStores() {
        // TODO: get from device
        Store store = new Store();
        store.setId("jstore");
        store.setDisplayName("Drombler JStore");
        store.setEndpoint("http://drombler-jstore-staging.us-east-1.elasticbeanstalk.com/webresources");
//        store.setEndpoint("http://localhost:5000/webresources");
        List<Store> stores = new ArrayList<>();
        stores.add(store);
        return stores;
    }
    // addStore()
    // removeStore()

    public List<ApplicationId> getSelectedApplications() {
        // TODO: get from device
        List<ApplicationId> applicationIds = new ArrayList<>();
        ApplicationId jstoreClientService = new ApplicationId();
        jstoreClientService.setVendorId("drombler");
        jstoreClientService.setVendorApplicationId("drombler-jstore-client-service");
        applicationIds.add(jstoreClientService);
        ApplicationId jstoreClient = new ApplicationId();
        jstoreClient.setVendorId("drombler");
        jstoreClient.setVendorApplicationId("drombler-jstore-client");
        applicationIds.add(jstoreClient);
        return applicationIds;
    }
    // addSelectedApplication()
    // removeSelectedApplication()

    //...

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
