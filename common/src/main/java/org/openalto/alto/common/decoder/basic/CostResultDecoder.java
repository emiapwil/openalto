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

import org.openalto.alto.common.decoder.ALTODecoder;
import org.openalto.alto.common.decoder.ALTOChainDecoder;


public abstract class CostResultDecoder<T>
        extends ALTOChainDecoder<ALTOData<MetaData, CostResult<T>>>{

    public static final String CATEGORY_ADDR = "addr";
    public static final String CATEGORY_COST = "cost";
    public static final String CATEGORY_META = "meta";

    private String m_dataFieldName;

    public CostResultDecoder(String dataFieldName) {
        m_dataFieldName = dataFieldName;

        this.add(CATEGORY_COST, "ordinal", new OrdinalCostDecoder());
        this.add(CATEGORY_COST, "numerical", new NumericalCostDecoder());
        this.add(CATEGORY_META, "cost-type", new CostTypeDecoder());
    }

    @Override
    public ALTOData<MetaData, CostResult<T>> decode(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return this.decode(mapper.readTree(text));
        } catch (Exception e) {
            //TODO
        }
        return null;
    }

    @Override
    public ALTOData<MetaData, CostResult<T>> decode(JsonNode node) {
        if ((node == null) || (!node.isObject()))
            return null;

        try {
            ObjectNode data = (ObjectNode)node;

            MetaData meta = decodeMetaData(data.get("meta"));
            CostType costType = (CostType)meta.get("cost-type");
            if (costType == null)
                return null;

            CostResult<T> result;
            result = decodeCostMatrix(data.get(m_dataFieldName), costType);

            return new ALTOData<MetaData, CostResult<T>>(meta, result);
        } catch (Exception e) {
        }
        return null;
    }

    public MetaData decodeMetaData(JsonNode node) {
        if ((node == null) || (!node.isObject()))
            return null;
        ObjectNode metaNode = (ObjectNode)node;
        MetaData meta = new MetaData();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = metaNode.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            String field = entry.getKey();
            JsonNode target = entry.getValue();

            ALTODecoder<?> decoder = this.get(CATEGORY_META, field);
            if (decoder == null) {
                meta.put(field, target.toString());
                continue;
            }

            Object decoded = decoder.decode(target);
            if (decoded == null) {
                continue;
            }

            meta.put(field, decoded);
        }

        return meta;
    }

    public CostResult<T> decodeCostMatrix(JsonNode node, CostType costType) {
        if ((node == null) || (!node.isObject()))
            return null;
        ObjectNode mapNode = (ObjectNode)node;
        CostResult<T> result = new CostResult<T>();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = mapNode.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            String source = entry.getKey();
            JsonNode subnode = entry.getValue();

            if ((subnode == null) || (!subnode.isObject())) {
                //TODO
                continue;
            }

            T srcAddr = decodeAddress(source);

            if (srcAddr == null)
                continue;

            Map<T, Object> costs;
            costs = decodeCostFrom(srcAddr, (ObjectNode)subnode, costType);

            if (costs == null)
                continue;

            result.addCosts(srcAddr, costs);
        }
        return result;
    }

    public Map<T, Object> decodeCostFrom(T srcAddr,
                                         ObjectNode costsNode,
                                         CostType costType) {
        if (costsNode == null)
            return null;

        Map<T, Object> costRow;
        costRow = new HashMap<T, Object>();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = costsNode.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            String destination = entry.getKey();
            JsonNode costNode = entry.getValue();

            T dstAddr = decodeAddress(destination);

            if (dstAddr == null) {
                continue;
            }

            if (!canConnect(srcAddr, dstAddr)) {
                continue;
            }

            Object cost = decodeCost(costNode, costType);
            if (cost == null)
                continue;

            costRow.put(dstAddr, cost);
        }
        return costRow;
    }

    public Object decodeCost(JsonNode costNode, CostType costType) {
        ALTODecoder<?> decoder = this.get(CATEGORY_COST, costType.getMode());
        if (decoder == null)
            return null;
        return decoder.decode(costNode);
    }

    public abstract T decodeAddress(String addr);

    public abstract boolean canConnect(T srcAddr, T dstAddr);
}
