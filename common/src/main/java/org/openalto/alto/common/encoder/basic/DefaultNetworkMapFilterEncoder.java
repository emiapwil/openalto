package org.openalto.alto.common.encoder.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;

import org.openalto.alto.common.encoder.ALTOEncoder;

public class DefaultNetworkMapFilterEncoder implements ALTOEncoder {

    @Override
    public boolean canEncode(Object obj) {
        if (obj == null)
            return true;
        return (obj instanceof DefaultNetworkMapFilter);
    }

    @Override
    public String encodeAsString(Object obj) {
        if (!canEncode(obj))
            return null;
        DefaultNetworkMapFilter param = (DefaultNetworkMapFilter)obj;

        JsonNode node = encode(param);
        if (node == null)
            return null;
        return node.toString();
    }

    @Override
    public JsonNode encode(Object obj) {
        if (!canEncode(obj))
            return null;
        DefaultNetworkMapFilter param = (DefaultNetworkMapFilter)obj;

        if (param == null)
            return null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree("{}");

            //pid names are essential but can be empty
            // See https://tools.ietf.org/html/rfc7285#section-11.3.1.3
            Collection<String> pidNames = param.getPidNames();
            if (pidNames == null) {
                return null;
            }
            ArrayNode pidsNode = (ArrayNode)node.withArray("pids");
            for (String pid: pidNames) {
                pidsNode.add(pid);
            }

            //address types are not essential
            Collection<String> addrTypes = param.getAddrTypes();
            if ((addrTypes == null) || (addrTypes.size() == 0)) {
                return node;
            }
            ArrayNode typesNode = (ArrayNode)node.withArray("address-types");
            for (String addrType: addrTypes) {
                typesNode.add(addrType);
            }

            return node;
        } catch (Exception e) {
        }
        return null;
    }
}