package org.openalto.alto.client.wrapper;

import javax.ws.rs.client.Client;

import org.openalto.alto.common.encoder.ALTOEncoder;
import org.openalto.alto.common.resource.ResourceEntry;
import org.openalto.alto.common.resource.ResourceType;

import org.openalto.alto.client.ALTORequest;
import org.openalto.alto.client.ALTOResponseParser;

public class FilteredNetworkMapRequestBuilder extends PostRequestBuilder {

    public FilteredNetworkMapRequestBuilder(Client client,
                                            ALTOResponseParser parser,
                                            ALTOEncoder encoder) {
        super(client, parser, encoder);
    }

    @Override
    public boolean canRequest(ResourceEntry entry) {
        if ((entry == null) || (entry.getType() == null))
            return false;
        return (entry.getType().equals(ResourceType.FILTERED_NETWORK_MAP_TYPE));
    }

    @Override
    public ALTORequest request(ResourceEntry resource, Object params) {
        return super.request(resource, params);
    }
}
