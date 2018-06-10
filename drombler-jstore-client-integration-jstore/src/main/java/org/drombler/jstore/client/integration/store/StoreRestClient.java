package org.drombler.jstore.client.integration.store;

import feign.Headers;
import feign.RequestLine;
import org.drombler.jstore.protocol.json.ApplicationVersionSearchRequest;
import org.drombler.jstore.protocol.json.ApplicationVersionSearchResponse;

public interface StoreRestClient {

    @RequestLine("GET /actuator/info")
    String ping();

    @RequestLine("POST /v1/application-version-search")
    @Headers("Content-Type: application/json")
    ApplicationVersionSearchResponse startApplicationVersionSearch(ApplicationVersionSearchRequest request);

}
