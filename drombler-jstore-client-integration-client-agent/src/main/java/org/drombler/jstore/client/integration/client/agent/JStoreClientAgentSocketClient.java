package org.drombler.jstore.client.integration.client.agent;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class JStoreClientAgentSocketClient implements AutoCloseable {
    private final Socket socket;
    private final OutputStreamWriter writer;
    private final ObjectMapper objectMapper;

    public JStoreClientAgentSocketClient(String host, int port, ObjectMapper objectMapper) throws IOException {
        this.socket = new Socket(host, port);
        this.writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
        this.objectMapper = objectMapper;
    }


    // ping()

    // getDeviceInfo()

    // getStores()
    // addStore()
    // removeStore()

    // getSelectedApplications()
    // addSelectedApplication()
    // removeSelectedApplication()

    //...

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
