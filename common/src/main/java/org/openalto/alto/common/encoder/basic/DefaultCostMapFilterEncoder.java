package org.openalto.alto.common.encoder.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.openalto.alto.common.encoder.ALTOEncoder;
import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.type.EndpointAddress;

public class DefaultCostMapFilterEncoder
        extends CostFilterEncoder<String> {

    public DefaultCostMapFilterEncoder() {
        super("pids");
    }
}
