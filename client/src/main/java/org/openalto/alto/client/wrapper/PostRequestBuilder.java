package org.openalto.alto.client.wrapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;

import javax.ws.rs.core.MediaType;

import org.openalto.alto.common.encoder.ALTOEncoder;
import org.openalto.alto.common.resource.ResourceEntry;
import org.openalto.alto.common.resource.ResourceType;
import org.openalto.alto.common.resource.ResourceTypeMapper;

import org.openalto.alto.client.ALTORequest;
import org.openalto.alto.client.ALTORequestBuilder;
import org.openalto.alto.client.ALTOResponseParser;

public abstract class PostRequestBuilder extends ALTORequestBuilder {

    private ALTOEncoder m_encoder;

    public PostRequestBuilder(Client client, ALTOResponseParser parser, ALTOEncoder encoder) {
        super(client, parser);
        m_encoder = encoder;
    }

    @Override
    public ALTORequest request(ResourceEntry resource, Object params) {
        if (!canRequest(resource))
            return null;
        if (!m_encoder.canEncode(params))
            return null;

        try {
            String encoded = m_encoder.encodeAsString(params);

            if (encoded == null)
                return null;

            ResourceTypeMapper mapper = this.getResourceTypeMapper();
            ResourceType resourceType = resource.getType();
            MediaType paramType = mapper.getParamType(resourceType);

            Entity<String> entity = Entity.entity(encoded, paramType);
            return _request(resource, params, entity);
        } catch (Exception e) {
        }
        return null;
    }
}
