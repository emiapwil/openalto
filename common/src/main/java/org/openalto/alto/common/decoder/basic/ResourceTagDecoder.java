package org.openalto.alto.common.decoder.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.openalto.alto.common.type.ResourceTag;
import org.openalto.alto.common.decoder.ALTODecoder;

import org.openalto.alto.common.standard.RFC7285;

public class ResourceTagDecoder implements ALTODecoder<ResourceTag> {

    public static final String RESOURCE_ID = RFC7285.VTAG_RESOURCE_ID;
    public static final String TAG = RFC7285.VTAG_TAG;

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
            String resourceId = node.get(RESOURCE_ID).asText();
            String tag = node.get(TAG).asText();
            return new ResourceTag(resourceId, tag);
        } catch (Exception e) {
        }
        return null;
    }
}
