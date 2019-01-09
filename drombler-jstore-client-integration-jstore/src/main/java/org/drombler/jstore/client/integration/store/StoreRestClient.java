package org.drombler.jstore.client.integration.store;

import feign.Headers;
import feign.RequestLine;
import org.drombler.jstore.protocol.json.ApplicationVersionSearchRequest;
import org.drombler.jstore.protocol.json.ApplicationVersionSearchResponse;
import org.drombler.jstore.protocol.json.CreateVendorRequest;
import org.drombler.jstore.protocol.json.JreVersionSearchRequest;
import org.drombler.jstore.protocol.json.JreVersionSearchResponse;

public interface StoreRestClient {

    @RequestLine("GET /actuator/info")
    String ping();

    @RequestLine("POST /v1/managed-components/application-version-search")
    @Headers("Content-Type: application/json")
    ApplicationVersionSearchResponse startApplicationVersionSearch(ApplicationVersionSearchRequest request);

    @RequestLine("POST /v1/managed-components/jre-version-search")
    @Headers("Content-Type: application/json")
    JreVersionSearchResponse startJreVersionSearch(JreVersionSearchRequest request);
    
    @RequestLine("POST /v1/vendors")
    @Headers("Content-Type: application/json")
    void createVendor(CreateVendorRequest request);

}
