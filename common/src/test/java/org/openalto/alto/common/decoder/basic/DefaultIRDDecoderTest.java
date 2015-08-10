package org.openalto.alto.common.decoder.basic;

import org.junit.Test;

import java.net.InetAddress;
import java.net.URI;

import java.util.Collection;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.openalto.alto.common.resource.ResourceEntry;
import org.openalto.alto.common.resource.ResourceType;

import org.openalto.alto.common.ALTOMediaType;
import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.Capability;
import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.type.ResourceTag;
import org.openalto.alto.common.type.EndpointAddress;

public class DefaultIRDDecoderTest {

    /*
     *  {
     *      "meta" : {
     *          "cost-types": {
     *              "num-routing": {
     *                  "cost-mode"  : "numerical",
     *                  "cost-metric": "routingcost",
     *                  "description": "My default"
     *              },
     *              "num-hop":     {
     *                  "cost-mode"  : "numerical",
     *                  "cost-metric": "hopcount"
     *              },
     *              "ord-routing": {
     *                  "cost-mode"  : "ordinal",
     *                  "cost-metric": "routingcost"
     *              },
     *              "ord-hop":     {
     *                  "cost-mode"  : "ordinal",
     *                  "cost-metric": "hopcount"
     *              }
     *          },
     *          "default-alto-network-map" : "my-default-network-map"
     *      },
     *      "resources" : {
     *          "my-default-network-map" : {
     *              "uri" : "http://alto.example.com/networkmap",
     *              "media-type" : "application/alto-networkmap+json"
     *          },
     *          "numerical-routing-cost-map" : {
     *              "uri" : "http://alto.example.com/costmap/num/routingcost",
     *              "media-type" : "application/alto-costmap+json",
     *              "capabilities" : {
     *                  "cost-type-names" : [ "num-routing" ]
     *              },
     *              "uses": [ "my-default-network-map" ]
     *          },
     *      }
     *  }
     * */

    @Test
    public void testDecode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree("{}");

        URI NM_URI = new URI("http://alto.example.com/networkmap");
        URI CM_URI = new URI("http://alto.example.com/costmap/num/routingcost");

        Object[] bindings[] = {
            { "num-routing", new CostType("numerical", "routingcost") },
            { "num-hop", new CostType("numerical", "hopcount") },
            { "ord-routing", new CostType("ordinal", "routingcost") },
            { "ord-hop", new CostType("ordinal", "hopcount") }
        };

        JsonNode costTypesNode = root.with("meta").with("cost-types");
        for (Object[] binding: bindings) {
            String name = (String)binding[0];
            CostType type = (CostType)binding[1];

            ObjectNode typeNode = (ObjectNode)costTypesNode.with(name);
            typeNode.put("cost-mode", type.getMode());
            typeNode.put("cost-metric", type.getMetric());
        }

        JsonNode resourcesNode = root.with("resources");
        ObjectNode nmNode = (ObjectNode)resourcesNode.with("my-default-network-map");
        nmNode.put("uri", NM_URI.toString());
        nmNode.put("media-type", ALTOMediaType.NETWORK_MAP);

        ObjectNode cmNode = (ObjectNode)resourcesNode.with("numerical-routingcost-map");
        cmNode.put("uri", CM_URI.toString());
        cmNode.put("media-type", ALTOMediaType.COST_MAP);
        ObjectNode capNode = (ObjectNode)cmNode.with("capabilities");
        ArrayNode ctnNode = (ArrayNode)capNode.withArray("cost-type-names");
        ctnNode.add("num-routing");

        ArrayNode usesNode = (ArrayNode)cmNode.withArray("uses");
        usesNode.add("my-default-network-map");

        System.out.println(root.toString());

        DefaultIRDDecoder decoder = new DefaultIRDDecoder();
        ALTOData<MetaData, List<ResourceEntry>> data;
        data = decoder.decode(root.toString());
        assertNotNull(data);

        MetaData meta = data.getMeta();

        List<ResourceEntry> resources = data.getData();
        assertEquals(resources.size(), 2);
        for (ResourceEntry resource: resources) {
            if (resource.getURI().equals(NM_URI)) {
                assertEquals(resource.getResourceId(), "my-default-network-map");
                assertEquals(resource.getType(), ResourceType.NETWORK_MAP_TYPE);
            } else if (resource.getURI().equals(CM_URI)) {
                assertEquals(resource.getResourceId(), "numerical-routingcost-map");
                assertEquals(resource.getType(), ResourceType.COST_MAP_TYPE);
                assertEquals(resource.getCapabilities().size(), 1);
                Set<Capability<?>> capabilities = resource.getCapabilities();
                CostType numRoutingCostType = (CostType)bindings[0][1];
                assertTrue(capabilities.contains(new Capability<CostType>("cost-type", numRoutingCostType)));

                Set<String> uses = (Set<String>)resource.getData("uses");
                assertEquals(uses.size(), 1);
                assertTrue(uses.contains("my-default-network-map"));
            } else {
                assertTrue(false);
            }
        }
    }
}
