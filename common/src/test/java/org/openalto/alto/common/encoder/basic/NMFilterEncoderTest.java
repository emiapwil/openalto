package org.openalto.alto.common.encoder.basic;

import org.junit.Test;

import java.util.Set;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openalto.alto.common.encoder.ALTOEncoder;

public class NMFilterEncoderTest {

    @Test
    public void testEncode() throws Exception {
        DefaultNetworkMapFilter param;

        Set<String> pids = new HashSet<String>();
        pids.add("pid1");
        pids.add("pid2");

        Set<String> addrTypes = new HashSet<String>();
        addrTypes.add("ipv4");
        addrTypes.add("ipv6");

        param = new DefaultNetworkMapFilter(pids, addrTypes);
        ALTOEncoder encoder = new DefaultNetworkMapFilterEncoder();

        System.out.println(encoder.encodeAsString((Object)param));
    }
}

