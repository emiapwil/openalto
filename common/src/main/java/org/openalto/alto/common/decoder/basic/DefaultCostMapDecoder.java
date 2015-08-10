package org.openalto.alto.common.decoder.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.type.MetaData;

import org.openalto.alto.common.decoder.ALTODecoder;
import org.openalto.alto.common.decoder.ALTOChainDecoder;


public class DefaultCostMapDecoder
        extends ALTOChainDecoder<ALTOData<MetaData, DefaultCostMap>>{

    private CostResultDecoder<String>
    m_decoder = new CostResultDecoder<String>("cost-map") {

        @Override
        public String decodeAddress(String addr) {
            return addr;
        }

        @Override
        public boolean canConnect(String srcAddr,
                                  String dstAddr) {
            return true;
        }

    };

    public DefaultCostMapDecoder() {
    }

    @Override
    public ALTOData<MetaData, DefaultCostMap> decode(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return this.decode(mapper.readTree(text));
        } catch (Exception e) {
            //TODO
        }
        return null;
    }

    @Override
    public ALTOData<MetaData, DefaultCostMap> decode(JsonNode node) {
        if ((node == null) || (!node.isObject()))
            return null;

        try {
            ALTOData<MetaData, CostResult<String>> decoded;
            decoded = m_decoder.decode(node);

            if (decoded == null)
                return null;

            MetaData meta = decoded.getMeta();
            DefaultCostMap result;
            result = new DefaultCostMap(decoded.getData());

            return new ALTOData<MetaData, DefaultCostMap>(meta, result);
        } catch (Exception e) {
        }
        return null;
    }
}
