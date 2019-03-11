package org.drombler.jstore.client.integration.client.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.drombler.jstore.protocol.json.*;

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
//        store.setEndpoint("http://drombler-jstore-staging.us-east-1.elasticbeanstalk.com/api");
        store.setEndpoint("http://localhost:5000/api");
        List<Store> stores = new ArrayList<>();
        stores.add(store);
        return stores;
    }

    public SystemInfo getSystemInfo() {
        // TODO: get from device
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setOsName(System.getProperty("os.name"));
        systemInfo.setOsArch(System.getProperty("os.arch"));
        systemInfo.setOsVersion(System.getProperty("os.version"));
        return systemInfo;
    }
    // addStore()
    // removeStore()

    public List<SelectedApplication> getSelectedApplications() {
        // TODO: get from device
        List<SelectedApplication> selectedApplications = new ArrayList<>();
        SelectedApplication jstoreClientAgent = new SelectedApplication();
        ApplicationId jstoreClientAgentApplicationId = new ApplicationId();
        jstoreClientAgentApplicationId.setGroupId("org.drombler.jstore.client.agent");
        jstoreClientAgentApplicationId.setArtifactId("drombler-jstore-client-agent-application");

        jstoreClientAgent.setApplicationId(jstoreClientAgentApplicationId);
        selectedApplications.add(jstoreClientAgent);

        SelectedApplication jstoreClient = new SelectedApplication();
        ApplicationId jstoreClientApplicationId = new ApplicationId();
        jstoreClientApplicationId.setGroupId("org.drombler.jstore.client");
        jstoreClientApplicationId.setArtifactId("drombler-jstore-client-application");

        jstoreClient.setApplicationId(jstoreClientApplicationId);
        selectedApplications.add(jstoreClient);
        return selectedApplications;
    }
    // addSelectedApplication()
    // removeSelectedApplication()

    //...

    public List<SelectedJRE> getSelectedJREs() {
        // TODO: get from device
        List<SelectedJRE> selectedJREs = new ArrayList<>();
        SelectedJRE jre8 = new SelectedJRE();
        JreInfo jre8Info = new JreInfo();
        jre8Info.setJavaSpecificationVersion("8");
        jre8Info.setJreVendorId("oracle");
        jre8.setJreInfo(jre8Info);
        jre8.setInstalledImplementationVersion("8u171-b11");
        selectedJREs.add(jre8);

        SelectedJRE jre10 = new SelectedJRE();
        JreInfo jre10Info = new JreInfo();
        jre10Info.setJavaSpecificationVersion("10");
        jre10Info.setJreVendorId("oracle");
        jre10.setJreInfo(jre10Info);
        selectedJREs.add(jre10);

        return selectedJREs;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }


}
