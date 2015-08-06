package org.openalto.alto.common.decoder.basic;

import java.net.InetAddress;

import com.fasterxml.jackson.databind.JsonNode;

import org.openalto.alto.common.decoder.ALTODecoder;
import org.openalto.alto.common.type.EndpointAddress;

public class InetSubnetDecoder
             implements ALTODecoder<EndpointAddress<InetPrefix>> {

    private String m_family;
    private int m_maxLen;
    private Class<? extends InetAddress> m_class;

    public InetSubnetDecoder(String family,
                             int maxPrefixLen, Class<? extends InetAddress> c) {
        m_family = family;
        m_maxLen = maxPrefixLen;
        m_class = c;
    }

    @Override
    public EndpointAddress<InetPrefix> decode(String text) {
        try {
            String[] data = text.split("/");
            if (data.length > 2)
                return null;

            InetAddress addr = InetAddress.getByName(data[0]);
            if (!m_class.isInstance(addr))
                return null;

            int plen = (data.length == 1 ? m_maxLen : Integer.parseInt(data[1]));
            if (plen > m_maxLen)
                return null;

            InetPrefix prefix = new InetPrefix(addr, plen);
            return new EndpointAddress<InetPrefix>(m_family, prefix);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public EndpointAddress<InetPrefix> decode(JsonNode node) {
        if (node == null)
            return null;
        return decode(node.asText());
    }
}
