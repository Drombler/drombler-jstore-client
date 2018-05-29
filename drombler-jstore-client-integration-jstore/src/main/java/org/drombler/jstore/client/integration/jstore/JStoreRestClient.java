package org.drombler.jstore.client.integration.jstore;

import feign.RequestLine;
import org.drombler.jstore.protocol.json.ApplicationVersionSearchRequest;
import org.drombler.jstore.protocol.json.ApplicationVersionSearchResponse;

public interface JStoreRestClient {

    @RequestLine("POST /v1/application-version-search")
    ApplicationVersionSearchResponse startApplicationVersionSearch(ApplicationVersionSearchRequest request);

}
