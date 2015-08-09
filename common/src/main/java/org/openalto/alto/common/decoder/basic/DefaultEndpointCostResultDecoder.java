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


public class DefaultEndpointCostResultDecoder
        extends ALTOChainDecoder<ALTOData<MetaData, DefaultEndpointCostResult>>{

    public static final String CATEGORY_ADDR = "addr";
    public static final String CATEGORY_COST = "cost";
    public static final String CATEGORY_META = "meta";

    public DefaultEndpointCostResultDecoder() {
        this.add(CATEGORY_ADDR, "ipv4",
                 new InetAddressDecoder("ipv4", Inet4Address.class));
        this.add(CATEGORY_ADDR, "ipv6",
                 new InetAddressDecoder("ipv6", Inet6Address.class));
        this.add(CATEGORY_COST, "ordinal", new OrdinalCostDecoder());
        this.add(CATEGORY_COST, "numerical", new NumericalCostDecoder());
        this.add(CATEGORY_META, "cost-type", new CostTypeDecoder());
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
            ObjectNode data = (ObjectNode)node;

            MetaData meta = decodeMetaData(data.get("meta"));
            CostType costType = (CostType)meta.get("cost-type");
            if (costType == null)
                return null;

            DefaultEndpointCostResult result;
            result = decodeCostMatrix(data.get("endpoint-cost-map"), costType);

            return new ALTOData<MetaData, DefaultEndpointCostResult>(meta, result);
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

    public DefaultEndpointCostResult decodeCostMatrix(JsonNode node,
                                                      CostType costType) {
        if ((node == null) || (!node.isObject()))
            return null;
        ObjectNode mapNode = (ObjectNode)node;
        DefaultEndpointCostResult result = new DefaultEndpointCostResult();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = mapNode.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            String compactAddr = entry.getKey();
            JsonNode subnode = entry.getValue();

            if ((subnode == null) || (!subnode.isObject())) {
                //TODO
                continue;
            }

            EndpointAddress<?> source = decodeAddress(compactAddr);

            if (source == null)
                continue;

            Map<EndpointAddress<?>, Object> costs;
            costs = decodeCostFrom(source, (ObjectNode)subnode, costType);

            if (costs == null)
                continue;

            result.addCostFrom(source, costs);
        }
        return result;
    }

    public Map<EndpointAddress<?>, Object> decodeCostFrom(EndpointAddress<?> source,
                                                          ObjectNode costsNode,
                                                          CostType costType) {
        if (costsNode == null)
            return null;

        Map<EndpointAddress<?>, Object> costRow;
        costRow = new HashMap<EndpointAddress<?>, Object>();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = costsNode.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            String compactDst = entry.getKey();
            JsonNode costNode = entry.getValue();

            EndpointAddress<?> dstAddr = decodeAddress(compactDst);

            if (dstAddr == null) {
                continue;
            }
            if (!(dstAddr.getFamily().equals(source.getFamily()))) {
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
}
