package org.openalto.alto.common.decoder.basic;

import java.net.InetAddress;

import com.fasterxml.jackson.databind.JsonNode;

import org.openalto.alto.common.decoder.ALTODecoder;
import org.openalto.alto.common.type.EndpointAddress;
import org.openalto.alto.common.encoder.basic.InetAddressFixer;

public class InetAddressDecoder
        implements ALTODecoder<EndpointAddress<InetAddress>> {

    private String m_family;
    private Class<? extends InetAddress> m_class;

    public InetAddressDecoder(String family, Class<? extends InetAddress> c) {
        m_family = family;
        m_class = c;
    }

    @Override
    public EndpointAddress<InetAddress> decode(String text) {
        if (text == null)
            return null;
        try {
            InetAddress addr = InetAddress.getByName(text);
            if (!m_class.isInstance(addr))
                return null;
            return new InetAddressFixer(m_family, addr);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public EndpointAddress<InetAddress> decode(JsonNode node) {
        if (node == null)
            return null;
        return decode(node.asText());
    }
}
