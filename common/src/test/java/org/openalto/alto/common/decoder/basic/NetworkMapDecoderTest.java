package org.openalto.alto.common.decoder.basic;

import org.junit.Test;

import java.net.InetAddress;

import java.util.Set;

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

public class NetworkMapDecoderTest {

    @Test
    public void testDecode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode map = mapper.readTree("{}");

        ArrayNode array;
        array = (ArrayNode)map.with("network-map").with("PID1").withArray("ipv4");
        array.add("192.168.0.1");
        array.add("192.168.0.1");
        array.add("192.0.2.0/24");
        array.add("198.51.100.0/25");

        Object addrList1[] = {
            new EndpointAddress<InetPrefix>("ipv4", new InetPrefix(InetAddress.getByName("192.168.0.1"), 32)),
            new EndpointAddress<InetPrefix>("ipv4", new InetPrefix(InetAddress.getByName("192.168.0.1"), 32)),
            new EndpointAddress<InetPrefix>("ipv4", new InetPrefix(InetAddress.getByName("192.0.2.0"), 24)),
            new EndpointAddress<InetPrefix>("ipv4", new InetPrefix(InetAddress.getByName("198.51.100.0"), 25))
        };
        assertEquals(addrList1[0], addrList1[1]);

        array = (ArrayNode)map.with("network-map").with("PID2").withArray("ipv6");
        array.add("2001::1/64");
        array.add("::1/0");
        array.add("2001::2");

        Object addrList2[] = {
            new EndpointAddress<InetPrefix>("ipv6", new InetPrefix(InetAddress.getByName("2001::1"), 64)),
            new EndpointAddress<InetPrefix>("ipv6", new InetPrefix(InetAddress.getByName("::1"), 0)),
            new EndpointAddress<InetPrefix>("ipv6", new InetPrefix(InetAddress.getByName("2001::2"), 128))
        };

        DefaultNetworkMapDecoder decoder = new DefaultNetworkMapDecoder();
        ALTOData<MetaData, DefaultNetworkMap> data = decoder.decode(map.toString());
        assertNotNull(data);
        DefaultNetworkMap nm = data.getData();
        assertNotNull(nm);

        Set<EndpointAddress<?>> node;

        node = nm.getNode("PID1");
        assertNotNull(node);
        assertEquals(node.size(), 3);
        for (Object addr: addrList1) {
            assertTrue(node.contains(addr));
        }

        node = nm.getNode("PID2");
        assertNotNull(node);
        assertEquals(node.size(), 3);
        for (Object addr: addrList2) {
            assertTrue(node.contains(addr));
        }
    }
}
