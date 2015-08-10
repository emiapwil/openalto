package org.openalto.alto.common.decoder.basic;

import org.junit.Test;

import java.net.InetAddress;

import java.util.Arrays;
import java.util.Collection;
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
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.type.EndpointAddress;
import org.openalto.alto.common.type.ResourceTag;

public class EndpointPropertyResultDecoderTest {

    /*
     *  {
     *      "meta" : {
     *          "dependent-vtags" : [
     *              {
     *                  "resource-id": "my-default-networkmap",
     *                  "tag": "7915dc0290c2705481c491a2b4ffbec482b3cf62"
     *              }
     *          ]
     *      },
     *      "endpoint-properties": {
     *          "ipv4:192.0.2.34": {
     *              "my-default-networkmap.pid": "PID1",
     *              "priv:ietf-example-prop": "1"
     *          },
     *          "ipv4:203.0.113.129": {
     *              "my-default-networkmap.pid": "PID3"
     *          }
     *      }
     * }
     */
    @Test
    public void testDecode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode map = mapper.readTree("{}");

        ResourceTag vtag = new ResourceTag("my-default-networkmap", "7915dc0290c2705481c491a2b4ffbec482b3cf62");
        ObjectNode vtagNode = (ObjectNode)mapper.readTree("{}");
        vtagNode.put("resource-id", vtag.getResourceId());
        vtagNode.put("tag", vtag.getTag());

        ArrayNode vtagsNode = (ArrayNode)map.with("meta").withArray("dependent-vtags");
        vtagsNode.add(vtagNode);

        ObjectNode epsMap = (ObjectNode)map.with("endpoint-properties");
        ObjectNode node;

        String addrStr[] = {
            "ipv4:192.0.2.2",
            "ipv4:203.0.113.129"
        };
        EndpointAddress<?> addr[] = {
            new EndpointAddress<InetAddress>("ipv4", InetAddress.getByName("192.0.2.2")),
            new EndpointAddress<InetAddress>("ipv4", InetAddress.getByName("203.0.113.129")),
        };

        node = (ObjectNode)epsMap.with(addrStr[0]);
        node.put("my-default-networkmap.pid", "PID1");
        node.put("priv:ietf-example-prop", "1");

        node = (ObjectNode)epsMap.with(addrStr[1]);
        node.put("my-default-networkmap.pid", "PID3");

        DefaultEndpointPropertyResultDecoder decoder;
        ALTOData<MetaData, DefaultEndpointPropertyResult> data;

        System.out.println(map.toString());

        decoder = new DefaultEndpointPropertyResultDecoder();
        data = decoder.decode(map.toString());
        assertNotNull(data);

        MetaData meta = data.getMeta();
        DefaultEndpointPropertyResult epr = data.getData();
        assertNotNull(meta);
        assertNotNull(epr);

        assertNotNull(meta.get("dependent-vtags"));

        Collection<ResourceTag> vtags = (Collection<ResourceTag>)meta.get("dependent-vtags");
        assertEquals(vtags.size(), 1);
        Object lhs[] = vtags.toArray();
        Object rhs[] = { vtag };
        assertTrue(Arrays.deepEquals(lhs, rhs));

        Map<String, Object> properties;

        properties = epr.getProperties(addr[0]);
        assertNotNull(properties);
        assertEquals(properties.size(), 2);

        assertEquals(properties.get("my-default-networkmap.pid"), "PID1");
        assertEquals(properties.get("priv:ietf-example-prop"), "1");

        properties = epr.getProperties(addr[1]);
        assertNotNull(properties);
        assertEquals(properties.size(), 1);

        assertEquals(properties.get("my-default-networkmap.pid"), "PID3");
    }
}
