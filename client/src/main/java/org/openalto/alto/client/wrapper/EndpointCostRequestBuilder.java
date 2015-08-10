package org.openalto.alto.client.wrapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;

import javax.ws.rs.core.MediaType;

import org.openalto.alto.common.encoder.ALTOEncoder;
import org.openalto.alto.common.resource.ResourceEntry;
import org.openalto.alto.common.resource.ResourceType;
import org.openalto.alto.common.resource.ResourceTypeMapper;

import org.openalto.alto.client.ALTORequest;
import org.openalto.alto.client.ALTOResponseParser;

public class EndpointCostRequestBuilder extends PostRequestBuilder {

    public EndpointCostRequestBuilder(Client client,
                                      ALTOResponseParser parser,
                                      ALTOEncoder encoder) {
        super(client, parser, encoder);
    }

    @Override
    public boolean canRequest(ResourceEntry entry) {
        if ((entry == null) || (entry.getType() == null))
            return false;
        return (entry.getType().equals(ResourceType.ENDPOINT_COST_SERVICE_TYPE));
    }

    @Override
    public ALTORequest request(ResourceEntry resource, Object params) {
        return super.request(resource, params);
    }
}
