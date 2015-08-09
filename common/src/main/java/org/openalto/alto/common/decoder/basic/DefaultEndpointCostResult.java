package org.openalto.alto.common.decoder.basic;

import java.util.Map;
import java.util.HashMap;

import org.openalto.alto.common.type.EndpointAddress;

public class DefaultEndpointCostResult {

    private Map<EndpointAddress<?>, Map<EndpointAddress<?>, Object>>
    m_costMatrix = new HashMap<EndpointAddress<?>, Map<EndpointAddress<?>, Object>>();

    public DefaultEndpointCostResult() {
    }

    public DefaultEndpointCostResult(DefaultEndpointCostResult rhs) {
        m_costMatrix = rhs.getCostMatrix();
    }

    public Map<EndpointAddress<?>, Map<EndpointAddress<?>, Object>> getCostMatrix() {
        return m_costMatrix;
    }

    public Map<EndpointAddress<?>, Object> getCostFrom(EndpointAddress<?> source) {
        return m_costMatrix.get(source);
    }

    public Object getCost(EndpointAddress<?> source,
                          EndpointAddress<?> destination) {
        Map<EndpointAddress<?>, Object> costs = getCostFrom(source);
        if (costs == null)
            return null;
        return  costs.get(destination);
    }

    public void addCost(EndpointAddress<?> source,
                        EndpointAddress<?> destination, Object cost) {
        if (!m_costMatrix.containsKey(source)) {
            m_costMatrix.put(source, new HashMap<EndpointAddress<?>, Object>());
        }
        Map<EndpointAddress<?>, Object> map = m_costMatrix.get(source);
        map.put(destination, cost);
    }

    public void addCostFrom(EndpointAddress<?> source,
                            Map<? extends EndpointAddress<?>, ? extends Object> costs) {
        if (!m_costMatrix.containsKey(source)) {
            m_costMatrix.put(source, new HashMap<EndpointAddress<?>, Object>());
        }
        Map<EndpointAddress<?>, Object> map = m_costMatrix.get(source);
        map.putAll(costs);
    }
}
