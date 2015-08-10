package org.openalto.alto.common.encoder.basic;

import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.type.EndpointAddress;
import org.openalto.alto.common.encoder.ALTOEncoder;
import org.openalto.alto.common.encoder.basic.InetAddressFixer;

public class ECSParamEncoderTest {

    @Test
    public void testEncode() throws Exception {
        CostType costType = new CostType("ordinal", "routingcost");
        DefaultEndpointCostParam param = new DefaultEndpointCostParam(costType);
        ALTOEncoder encoder = new DefaultEndpointCostParamEncoder();

        InetAddressFixer srcs[] = {
            new InetAddressFixer("ipv4", InetAddress.getByName("192.0.2.2"))
        };

        InetAddressFixer dsts[] = {
            new InetAddressFixer("ipv4", InetAddress.getByName("192.0.2.89")),
            new InetAddressFixer("ipv4", InetAddress.getByName("198.51.100.34")),
            new InetAddressFixer("ipv4", InetAddress.getByName("203.0.113.45"))
        };

        for (InetAddressFixer addr: srcs) {
            param.addSource(addr);
        }

        for (InetAddressFixer addr: dsts) {
            param.addDestination(addr);
        }

        System.out.println(encoder.encodeAsString((Object)param));
    }
}

