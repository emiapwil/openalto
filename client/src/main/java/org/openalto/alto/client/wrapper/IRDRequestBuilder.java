package org.openalto.alto.client.wrapper;

import javax.ws.rs.client.Client;

import org.openalto.alto.common.resource.ResourceEntry;
import org.openalto.alto.common.resource.ResourceType;

import org.openalto.alto.client.ALTORequest;
import org.openalto.alto.client.ALTORequestBuilder;
import org.openalto.alto.client.ALTOResponseParser;

public class IRDRequestBuilder extends ALTORequestBuilder {

    public IRDRequestBuilder(Client client, ALTOResponseParser parser) {
        super(client, parser);
    }

    @Override
    public boolean canRequest(ResourceEntry entry) {
        if ((entry == null) || (entry.getType() == null))
            return false;
        return (entry.getType().equals(ResourceType.DIRECTORY_TYPE));
    }

    @Override
    public ALTORequest request(ResourceEntry resource, Object params) {
        return _request(resource, params, null);
    }
}
