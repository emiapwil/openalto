package org.openalto.alto.common.decoder.basic;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.openalto.alto.common.type.Capability;

import org.openalto.alto.common.decoder.ALTODecoder;

import org.openalto.alto.common.standard.RFC7285;

public class PropCapabilityDecoder
        implements ALTODecoder<Set<Capability<String>>> {

    public static final String PROPERTY = RFC7285.CAPABILITY_PROP_TYPE;

    @Override
    public Set<Capability<String>> decode(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return decode(mapper.readTree(text));
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Set<Capability<String>> decode(JsonNode node) {
        if ((node == null) || (!node.isArray()))
            return null;

        ArrayNode propsNode = (ArrayNode)node;
        Set<Capability<String>> props = new HashSet<Capability<String>>();
        for (Iterator<JsonNode> itr = propsNode.elements(); itr.hasNext(); ) {
            JsonNode propNode = itr.next();

            if ((propNode == null) || (!propNode.isTextual()))
                continue;

            Capability<String> capability;
            capability = new Capability<String>(PROPERTY, propNode.asText());
            props.add(capability);
        }
        return props;
    }
}
