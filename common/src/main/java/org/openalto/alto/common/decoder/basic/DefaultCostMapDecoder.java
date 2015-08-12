package org.openalto.alto.common.decoder.basic;

import java.util.Collection;
import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.type.ResourceTag;

import org.openalto.alto.common.decoder.ALTODecoder;

import org.openalto.alto.common.standard.RFC7285;

public class DefaultCostMapDecoder
        implements ALTODecoder<ALTOData<MetaData, DefaultCostMap>> {

    private CostResultDecoder<String>
    m_decoder = new CostResultDecoder<String>(RFC7285.COST_MAP_FIELD) {

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
        ResourceTagDecoder rtd = new ResourceTagDecoder();
        CollectionDecoder.CollectionCreator<ResourceTag> creator;
        creator = new CollectionDecoder.CollectionCreator<ResourceTag>() {
            @Override
            public Collection<ResourceTag> create() {
                return new HashSet<ResourceTag>();
            }
        };

        CollectionDecoder<ResourceTag> colDecoder;
        colDecoder = new CollectionDecoder<ResourceTag>(rtd, creator);

        m_decoder.add(CostResultDecoder.CATEGORY_META,
                      RFC7285.META_FIELD_DEPENDENT_VTAGS, colDecoder);

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
