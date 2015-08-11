package org.openalto.alto.common.decoder.basic;

import java.net.Inet4Address;
import java.net.Inet6Address;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.type.EndpointAddress;

import org.openalto.alto.common.decoder.ALTODecoder;
import org.openalto.alto.common.decoder.ALTOChainDecoder;

import org.openalto.alto.common.standard.RFC7285;

public class DefaultEndpointCostResultDecoder
        extends ALTOChainDecoder<ALTOData<MetaData, DefaultEndpointCostResult>>{

    private CostResultDecoder<EndpointAddress<?>>
    m_decoder = new CostResultDecoder<EndpointAddress<?>>(RFC7285.ENDPOINT_COST_FIELD) {

        @Override
        public EndpointAddress<?> decodeAddress(String compact) {
            String fullAddr[] = compact.split(":", 2);
            if (fullAddr.length != 2)
                return null;

            String family = fullAddr[0];
            String addr = fullAddr[1];

            try {
                ALTODecoder<? extends Object> subDecoder;
                subDecoder = this.get(CATEGORY_ADDR, family);
                if (subDecoder == null)
                    return null;

                ALTODecoder<EndpointAddress<?>> addrDecoder;
                addrDecoder = (ALTODecoder<EndpointAddress<?>>)subDecoder;

                return addrDecoder.decode(addr);
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        public boolean canConnect(EndpointAddress<?> srcAddr,
                                  EndpointAddress<?> dstAddr) {
            return (dstAddr.getFamily().equals(srcAddr.getFamily()));
        }

    };

    public DefaultEndpointCostResultDecoder() {
        m_decoder.add(CostResultDecoder.CATEGORY_ADDR, "ipv4",
                      new InetAddressDecoder("ipv4", Inet4Address.class));
        m_decoder.add(CostResultDecoder.CATEGORY_ADDR, "ipv6",
                      new InetAddressDecoder("ipv6", Inet6Address.class));
    }

    @Override
    public ALTOData<MetaData, DefaultEndpointCostResult> decode(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return this.decode(mapper.readTree(text));
        } catch (Exception e) {
            //TODO
        }
        return null;
    }

    @Override
    public ALTOData<MetaData, DefaultEndpointCostResult> decode(JsonNode node) {
        if ((node == null) || (!node.isObject()))
            return null;

        try {
            ALTOData<MetaData, CostResult<EndpointAddress<?>>> decoded;
            decoded = m_decoder.decode(node);

            if (decoded == null)
                return null;

            MetaData meta = decoded.getMeta();
            DefaultEndpointCostResult result;
            result = new DefaultEndpointCostResult(decoded.getData());

            return new ALTOData<MetaData, DefaultEndpointCostResult>(meta, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
