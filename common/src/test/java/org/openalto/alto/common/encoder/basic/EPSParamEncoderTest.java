package org.openalto.alto.common.encoder.basic;

import org.junit.Test;

import java.net.InetAddress;

import java.util.Set;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openalto.alto.common.type.EndpointAddress;

import org.openalto.alto.common.encoder.ALTOEncoder;

public class EPSParamEncoderTest {

    @Test
    public void testEncode() throws Exception {
        DefaultEndpointPropertyParam param;

        Set<EndpointAddress<?>> endpoints = new HashSet<EndpointAddress<?>>();
        InetAddressFixer addr[] = {
            new InetAddressFixer("ipv4", InetAddress.getByName("192.0.2.34")),
            new InetAddressFixer("ipv4", InetAddress.getByName("203.0.113.129"))
        };
        endpoints.add(addr[0]);
        endpoints.add(addr[1]);

        Set<String> properties = new HashSet<String>();
        properties.add("my-default-networkmap.pid");
        properties.add("priv:ietf-example-prop");

        param = new DefaultEndpointPropertyParam(endpoints, properties);
        ALTOEncoder encoder = new DefaultEndpointPropertyParamEncoder();

        System.out.println(encoder.encodeAsString((Object)param));
    }
}

