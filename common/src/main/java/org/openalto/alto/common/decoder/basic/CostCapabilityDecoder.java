package org.openalto.alto.common.decoder.basic;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.openalto.alto.common.type.Capability;
import org.openalto.alto.common.type.CostType;

import org.openalto.alto.common.decoder.ALTODecoder;

/*
 * NOT THREAD-SAFE!!!
 * */
public class CostCapabilityDecoder {

    private Map<String, CostType> m_mapping = new HashMap<String, CostType>();

    public ALTODecoder<?> metaDecoder() {
        return new ALTODecoder<Map<String, CostType>>() {
            @Override
            public Map<String, CostType> decode(String text) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    return decode(mapper.readTree(text));
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            public Map<String, CostType> decode(JsonNode node) {
                return buildMapping(node);
            }
        };
    }

    public ALTODecoder<Set<Capability<CostType>>> capabilityDecoder() {
        return new ALTODecoder<Set<Capability<CostType>>>() {
            @Override
            public Set<Capability<CostType>> decode(String text) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    return decode(mapper.readTree(text));
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            public Set<Capability<CostType>> decode(JsonNode node) {
                return mapNamesToTypes((ArrayNode)node);
            }
        };
    }

    protected Map<String, CostType> buildMapping(JsonNode node) {
        if ((node == null) || (!node.isObject()))
            return null;

        m_mapping.clear();

        ObjectNode typesNode = (ObjectNode)node;
        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = typesNode.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            try {
                String costTypeName = entry.getKey();
                ObjectNode costTypeNode = (ObjectNode)entry.getValue();

                String mode = costTypeNode.get("cost-mode").asText();
                String metric = costTypeNode.get("cost-metric").asText();
                String desc = "";
                if (costTypeNode.has("description"))
                    desc = costTypeNode.get("description").asText();

                CostType costType = new CostType(mode, metric, desc);

                m_mapping.put(costTypeName, costType);
            } catch (Exception e) {
            }
        }
        return m_mapping;
    }

    protected Set<Capability<CostType>> mapNamesToTypes(JsonNode node) {
        if ((node == null) || (!node.isArray()))
            return null;

        Set<Capability<CostType>> capabilities = new HashSet<Capability<CostType>>();
        ArrayNode namesNode = (ArrayNode)node;

        for (Iterator<JsonNode> itr = namesNode.elements(); itr.hasNext(); ) {
            JsonNode nameNode = itr.next();

            try {
                String name = nameNode.asText();
                CostType type = m_mapping.get(name);

                if (type == null)
                    continue;

                Capability<CostType> capability;
                capability = new Capability<CostType>("cost-type", type);

                capabilities.add(capability);
            } catch (Exception e) {
            }
        }
        return capabilities;
    }
}
