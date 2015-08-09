package org.openalto.alto.common.decoder.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.decoder.ALTODecoder;

public class CostTypeDecoder implements ALTODecoder<CostType> {

    @Override
    public CostType decode(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return decode(mapper.readTree(text));
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public CostType decode(JsonNode node) {
        try {
            String costMode = node.get("cost-mode").asText();
            String costMetric = node.get("cost-metric").asText();
            return new CostType(costMode, costMetric);
        } catch (Exception e) {
        }
        return null;
    }
}
