package org.openalto.alto.common.decoder.basic;

import java.util.Map;
import java.util.HashMap;

public class CostResult<T> {

    private Map<T, Map<T, Object>>
    m_costMatrix = new HashMap<T, Map<T, Object>>();

    public CostResult() {
    }

    public CostResult(CostResult rhs) {
        m_costMatrix = rhs.getCostMatrix();
    }

    public Map<T, Map<T, Object>> getCostMatrix() {
        return m_costMatrix;
    }

    public Map<T, Object> getCostFrom(T source) {
        return m_costMatrix.get(source);
    }

    public Object getCost(T source,
                          T destination) {
        Map<T, Object> costs = getCostFrom(source);
        if (costs == null)
            return null;
        return  costs.get(destination);
    }

    public void addCost(T source,
                        T destination, Object cost) {
        if (!m_costMatrix.containsKey(source)) {
            m_costMatrix.put(source, new HashMap<T, Object>());
        }
        Map<T, Object> map = m_costMatrix.get(source);
        map.put(destination, cost);
    }

    public void addCostFrom(T source,
                            Map<? extends T, ? extends Object> costs) {
        if (!m_costMatrix.containsKey(source)) {
            m_costMatrix.put(source, new HashMap<T, Object>());
        }
        Map<T, Object> map = m_costMatrix.get(source);
        map.putAll(costs);
    }
}
