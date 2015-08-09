package org.openalto.alto.common.encoder.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.openalto.alto.common.encoder.ALTOEncoder;
import org.openalto.alto.common.type.CostType;

public class CostFilterEncoder<T> implements ALTOEncoder {

    private String m_dataFieldName;

    public CostFilterEncoder(String dataFieldName) {
        m_dataFieldName = dataFieldName;
    }

    @Override
    public boolean canEncode(Object obj) {
        if (obj == null)
            return true;
        return (obj instanceof CostFilterParam);
    }

    @Override
    public String encodeAsString(Object obj) {
        if (!canEncode(obj))
            return null;
        CostFilterParam<T> param = (CostFilterParam<T>)obj;

        JsonNode node = encode(param);
        if (node == null)
            return null;
        return node.toString();
    }

    @Override
    public JsonNode encode(Object obj) {
        if (!canEncode(obj))
            return null;
        CostFilterParam<T> param = (CostFilterParam<T>)obj;

        if (param == null)
            return null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree("{}");

            //CostType is essential
            CostType costType = param.getCostType();
            ObjectNode costTypeNode = (ObjectNode)node.with("cost-type");
            costTypeNode.put("cost-mode", costType.getMode());
            costTypeNode.put("cost-metric", costType.getMetric());

            //Constraints are optional
            if (param.getConstraints().size() > 0) {
                ArrayNode constraintsNode = (ArrayNode)node.withArray("constraints");
                for (String constraint: param.getConstraints()) {
                    constraintsNode.add(constraint);
                }
            }

            //Endpoints are essential but srcs and dsts are optional
            ObjectNode endpointsNode = (ObjectNode)node.with(m_dataFieldName);
            if (param.getSources().size() > 0) {
                ArrayNode srcsNode = (ArrayNode)endpointsNode.withArray("srcs");
                for (T addr: param.getSources()) {
                    srcsNode.add(addr.toString());
                }
            }
            if (param.getDestinations().size() > 0) {
                ArrayNode dstsNode = (ArrayNode)endpointsNode.withArray("dsts");
                for (T addr: param.getDestinations()) {
                    dstsNode.add(addr.toString());
                }
            }
            return node;
        } catch (Exception e) {
        }
        return null;
    }
}
