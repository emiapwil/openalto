package org.openalto.alto.common.decoder.basic;

import com.fasterxml.jackson.databind.JsonNode;

import org.openalto.alto.common.decoder.ALTODecoder;

public class NumericalCostDecoder implements ALTODecoder<Double> {

    @Override
    public Double decode(String text) {
        try {
            return new Double(text);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Double decode(JsonNode node) {
        try {
            return node.asDouble();
        } catch (Exception e) {
        }
        return null;
    }
}
