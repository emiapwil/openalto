package org.openalto.alto.client.wrapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;

import org.openalto.alto.common.resource.ResourceEntry;
import org.openalto.alto.client.ALTORequest;
import org.openalto.alto.client.ALTORequestBuilder;
import org.openalto.alto.client.ALTOResponseParser;

public class NetworkMapRequestBuilder extends ALTORequestBuilder {

    public NetworkMapRequestBuilder(Client client, ALTOResponseParser parser) {
        super(client, parser);
    }

    @Override
    public ALTORequest request(ResourceEntry resource, Object params) {
        Invocation invocation = this.getClient()
                                    .target(resource.getURI())
                                    .request("application/alto-networkmap+json")
                                    .buildGet();
        return new ALTORequestBase(resource, params, getParser()).setInvocation(invocation);
    }
}
