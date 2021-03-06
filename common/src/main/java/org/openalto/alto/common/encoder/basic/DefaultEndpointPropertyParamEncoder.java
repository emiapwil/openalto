package org.openalto.alto.common.encoder.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Collection;

import org.openalto.alto.common.type.EndpointAddress;

import org.openalto.alto.common.encoder.ALTOEncoder;

import org.openalto.alto.common.standard.RFC7285;

public class DefaultEndpointPropertyParamEncoder implements ALTOEncoder {

    public static final String ENDPOINTS = RFC7285.PARAM_ENDPOINTS;
    public static final String PROPERTIES = RFC7285.PARAM_PROPERTIES;

    @Override
    public boolean canEncode(Object obj) {
        if (obj == null)
            return true;
        return (obj instanceof DefaultEndpointPropertyParam);
    }

    @Override
    public String encodeAsString(Object obj) {
        if (!canEncode(obj))
            return null;
        DefaultEndpointPropertyParam param = (DefaultEndpointPropertyParam)obj;

        JsonNode node = encode(param);
        if (node == null)
            return null;
        return node.toString();
    }

    @Override
    public JsonNode encode(Object obj) {
        if (!canEncode(obj))
            return null;
        DefaultEndpointPropertyParam param = (DefaultEndpointPropertyParam)obj;

        if (param == null)
            return null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree("{}");

            // See https://tools.ietf.org/html/rfc7285#section-11.4.1.3

            //endpoints are essential
            Collection<EndpointAddress<?>> endpoints = param.getEndpoints();
            if ((endpoints == null) || (endpoints.size() == 0)) {
                return null;
            }
            ArrayNode endpointsNode = (ArrayNode)node.withArray(ENDPOINTS);
            for (EndpointAddress<?> endpoint: endpoints) {
                endpointsNode.add(endpoint.toString());
            }

            //properties are essential
            Collection<String> properties = param.getProperties();
            if ((properties == null) || (properties.size() == 0)) {
                return node;
            }
            ArrayNode propertiesNode = (ArrayNode)node.withArray(PROPERTIES);
            for (String property: properties) {
                propertiesNode.add(property);
            }

            return node;
        } catch (Exception e) {
        }
        return null;
    }
}
