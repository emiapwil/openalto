package org.openalto.alto.common.decoder.basic;

import org.junit.Test;

import java.net.InetAddress;

import java.util.Collection;
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

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.type.ResourceTag;
import org.openalto.alto.common.type.EndpointAddress;

public class CostMapDecoderTest {

    /*
     *  "cost-map" : {
     *      "PID1": { "PID1": 1,  "PID2": 5,  "PID3": 10 },
     *      "PID2": { "PID1": 5,  "PID2": 1,  "PID3": 15 },
     *      "PID3": { "PID1": 20, "PID2": 15  }
     *  }
     * */

    @Test
    public void testDecode() throws Exception {
        _testDecode("numerical", Double.class);
        _testDecode("ordinal", Integer.class);
    }

    public void _testDecode(String mode, Class<? extends Number> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode map = mapper.readTree("{}");

        String pid[] = { "PID1", "PID2", "PID3" };
        ObjectNode costMap = (ObjectNode)map.with("cost-map");

        ObjectNode node;

        node = (ObjectNode)costMap.with(pid[0]);
        node.put(pid[0], new Integer(1));
        node.put(pid[1], new Integer(5));
        node.put(pid[2], new Integer(10));

        node = (ObjectNode)costMap.with(pid[1]);
        node.put(pid[0], new Integer(5));
        node.put(pid[1], new Integer(1));
        node.put(pid[2], new Integer(15));

        node = (ObjectNode)costMap.with(pid[2]);
        node.put(pid[0], new Integer(20));
        node.put(pid[1], new Integer(15));

        ObjectNode metaNode = (ObjectNode)map.with("meta");
        ArrayNode dependentVtags = (ArrayNode)metaNode.withArray("dependent-vtags");
        ResourceTag vtag = new ResourceTag("hello-world-map", "17793de59d3d4e98ad6f08f98273c241");
        ObjectNode vtagNode = (ObjectNode)mapper.readTree("{}");
        vtagNode.put("resource-id", vtag.getResourceId());
        vtagNode.put("tag", vtag.getTag());
        dependentVtags.add(vtagNode);

        ObjectNode costTypeNode = (ObjectNode)metaNode.with("cost-type");
        costTypeNode.put("cost-metric", "routingcost");
        costTypeNode.put("cost-mode", mode);

        CostType costType = new CostType(mode, "routingcost");

        DefaultCostMapDecoder decoder = new DefaultCostMapDecoder();
        ALTOData<MetaData, DefaultCostMap> data = decoder.decode(map.toString());
        assertNotNull(data);

        MetaData meta = data.getMeta();
        DefaultCostMap dcm = data.getData();
        assertNotNull(meta);
        assertNotNull(dcm);

        assertEquals(meta.get("cost-type"), costType);

        assertNotNull(meta.get("dependent-vtags"));

        Collection<ResourceTag> vtags = (Collection<ResourceTag>)meta.get("dependent-vtags");
        assertEquals(vtags.size(), 1);
        Object lhs[] = vtags.toArray();
        Object rhs[] = { vtag };
        assertTrue(Arrays.deepEquals(lhs, rhs));

        Map<String, Object> costs;

        costs = dcm.getCosts(pid[0]);
        assertNotNull(costs);
        assertEquals(costs.size(), 3);

        costs = dcm.getCosts(pid[1]);
        assertNotNull(costs);
        assertEquals(costs.size(), 3);

        costs = dcm.getCosts(pid[2]);
        assertNotNull(costs);
        assertEquals(costs.size(), 2);

        assertEquals(clazz.cast(dcm.getCost(pid[0], pid[0])).intValue(), 1);
        assertEquals(clazz.cast(dcm.getCost(pid[0], pid[1])).intValue(), 5);
        assertEquals(clazz.cast(dcm.getCost(pid[0], pid[2])).intValue(), 10);

        assertEquals(clazz.cast(dcm.getCost(pid[1], pid[0])).intValue(), 5);
        assertEquals(clazz.cast(dcm.getCost(pid[1], pid[1])).intValue(), 1);
        assertEquals(clazz.cast(dcm.getCost(pid[1], pid[2])).intValue(), 15);

        assertEquals(clazz.cast(dcm.getCost(pid[2], pid[0])).intValue(), 20);
        assertEquals(clazz.cast(dcm.getCost(pid[2], pid[1])).intValue(), 15);

        System.out.println(map.toString());
    }
}
