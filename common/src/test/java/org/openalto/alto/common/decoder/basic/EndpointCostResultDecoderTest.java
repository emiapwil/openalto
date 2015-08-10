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

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.type.EndpointAddress;

public class EndpointCostResultDecoderTest {

    /*
     * {
     *     "endpoint-cost-map": {
     *         "ipv4:192.0.2.2": {
     *             "ipv4:192.0.2.89": 1,
     *             "ipv4:198.51.100.134": 5,
     *             "ipv4:203.0.113.45": 10
     *         }
     *     },
     *     "meta": {
     *        "cost-type": {
     *            "cost-metric": "routingcost",
     *            "cost-mode": "numerical"
     *        }
     *     }
     * }
    */
    @Test
    public void testDecode() throws Exception {
        _testDecode("numerical", Double.class);
        _testDecode("ordinal", Integer.class);
    }

    public void _testDecode(String mode, Class<? extends Number> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode map = mapper.readTree("{}");

        ObjectNode ecsMap = (ObjectNode)map.with("endpoint-cost-map");
        ObjectNode node1 = (ObjectNode)ecsMap.with("ipv4:192.0.2.2");
        node1.put("ipv4:192.0.2.89", new Double(1));
        node1.put("ipv4:198.51.100.134", new Double(5));
        node1.put("ipv4:203.0.113.45", new Double(10));
        node1.put("ipv6:2001::1", new Double(10));

        ObjectNode metaNode = (ObjectNode)map.with("meta");
        ObjectNode costTypeNode = (ObjectNode)metaNode.with("cost-type");
        costTypeNode.put("cost-metric", "routingcost");
        costTypeNode.put("cost-mode", mode);

        CostType costType = new CostType(mode, "routingcost");

        DefaultEndpointCostResultDecoder decoder = new DefaultEndpointCostResultDecoder();
        ALTOData<MetaData, DefaultEndpointCostResult> data = decoder.decode(map.toString());
        assertNotNull(data);

        MetaData meta = data.getMeta();
        DefaultEndpointCostResult ecr = data.getData();
        assertNotNull(meta);
        assertNotNull(ecr);

        assertEquals(meta.get("cost-type"), costType);

        InetAddress sourceAddr = InetAddress.getByName("192.0.2.2");
        EndpointAddress<?> source = new EndpointAddress<InetAddress>("ipv4", sourceAddr);
        Map<EndpointAddress<?>, Object> costs = ecr.getCosts(source);
        assertNotNull(costs);
        assertEquals(costs.size(), 3);

        EndpointAddress<?> dst;

        dst = new EndpointAddress<InetAddress>("ipv4", InetAddress.getByName("192.0.2.89"));
        assertEquals(clazz.cast(ecr.getCost(source, dst)).intValue(), 1);
        dst = new EndpointAddress<InetAddress>("ipv4", InetAddress.getByName("198.51.100.134"));
        assertEquals(clazz.cast(ecr.getCost(source, dst)).intValue(), 5);
        dst = new EndpointAddress<InetAddress>("ipv4", InetAddress.getByName("203.0.113.45"));
        assertEquals(clazz.cast(ecr.getCost(source, dst)).intValue(), 10);

        System.out.println(map.toString());
    }
}
