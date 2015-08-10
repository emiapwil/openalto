package org.openalto.alto.common.decoder.basic;

import org.junit.Test;

import java.net.InetAddress;

import java.util.Set;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.openalto.alto.common.type.Capability;
import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.decoder.ALTODecoder;

public class CostCapabilityDecoderTest {

    @Test
    public void testDecode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Object[] bindings[] = {
            { "num-routing", new CostType("numerical", "routingcost") },
            { "num-hop", new CostType("numerical", "hopcount") },
            { "ord-routing", new CostType("ordinal", "routingcost") },
            { "ord-hop", new CostType("ordinal", "hopcount") }
        };
        JsonNode root = (ObjectNode)mapper.readTree("{}");
        JsonNode costTypesNode = root.with("cost-types");
        for (Object[] binding: bindings) {
            String name = (String)binding[0];
            CostType type = (CostType)binding[1];

            ObjectNode typeNode = (ObjectNode)costTypesNode.with(name);
            typeNode.put("cost-mode", type.getMode());
            typeNode.put("cost-metric", type.getMetric());
        }

        ArrayNode costTypeNamesNode = (ArrayNode)root.withArray("names");
        for (Object[] binding: bindings) {
            String name = (String)binding[0];
            costTypeNamesNode.add(name);
        }

        CostCapabilityDecoder decoder = new CostCapabilityDecoder();

        ALTODecoder<?> metaDecoder = decoder.metaDecoder();
        Map<String, CostType> decodedBindings;
        decodedBindings = (Map<String, CostType>)metaDecoder.decode(root.get("cost-types"));

        for (Object[] binding: bindings) {
            String name = (String)binding[0];
            assertEquals(binding[1], decodedBindings.get(name));
        }

        ALTODecoder<?> namesDecoder = decoder.capabilityDecoder();
        Set<Capability<CostType>> decodedTypes;
        decodedTypes = (Set<Capability<CostType>>)namesDecoder.decode(root.get("names"));
        assertEquals(decodedTypes.size(), bindings.length);
        for (Object[] binding: bindings) {
            CostType costType = (CostType)binding[1];
            assertTrue(decodedTypes.contains(new Capability<CostType>("cost-type", costType)));
        }

        System.out.println(root.toString());
    }
}
