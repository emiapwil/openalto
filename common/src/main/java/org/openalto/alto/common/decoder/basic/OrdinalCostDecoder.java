package org.openalto.alto.common.decoder.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.openalto.alto.common.decoder.ALTODecoder;

public class OrdinalCostDecoder implements ALTODecoder<Integer> {

    @Override
    public Integer decode(String text) {
        try {
            return new Integer(text);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Integer decode(JsonNode node) {
        try {
            return node.asInt();
        } catch (Exception e) {
        }
        return null;
    }
}
