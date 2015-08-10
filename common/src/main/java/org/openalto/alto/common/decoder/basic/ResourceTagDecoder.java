package org.openalto.alto.common.decoder.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.openalto.alto.common.type.ResourceTag;
import org.openalto.alto.common.decoder.ALTODecoder;

public class ResourceTagDecoder implements ALTODecoder<ResourceTag> {

    @Override
    public ResourceTag decode(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return decode(mapper.readTree(text));
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public ResourceTag decode(JsonNode node) {
        try {
            String resourceId = node.get("resource-id").asText();
            String tag = node.get("tag").asText();
            return new ResourceTag(resourceId, tag);
        } catch (Exception e) {
        }
        return null;
    }
}
