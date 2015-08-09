package org.openalto.alto.common.encoder.basic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.encoder.ALTOEncoder;

public class CostMapFilterEncoderTest {

    @Test
    public void testEncode() throws Exception {
        CostType costType = new CostType("ordinal", "routingcost");
        DefaultCostMapFilter param = new DefaultCostMapFilter(costType);
        ALTOEncoder encoder = new DefaultCostMapFilterEncoder();

        String srcs[] = {
            "PID1"
        };

        String dsts[] = {
            "PID1",
            "PID2",
            "PID3"
        };

        for (String addr: srcs) {
            param.addSource(addr);
        }

        for (String addr: dsts) {
            param.addDestination(addr);
        }

        System.out.println(encoder.encodeAsString((Object)param));
    }
}

