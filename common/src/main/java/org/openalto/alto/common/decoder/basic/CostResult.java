package org.openalto.alto.common.decoder.basic;

import java.util.Map;

import org.openalto.alto.common.type.Map2D;

public class CostResult<T> {

    private Map2D<T, T, Object> m_costMatrix = new Map2D<T, T, Object>();

    public CostResult() {
    }

    public CostResult(CostResult rhs) {
        m_costMatrix = new Map2D(rhs.getCostMatrix());
    }

    public Map<T, Map<T, Object>> getCostMatrix() {
        return m_costMatrix.getFullMap();
    }

    public Map<T, Object> getCosts(T source) {
        return m_costMatrix.get(source);
    }

    public Object getCost(T source, T destination) {
        return m_costMatrix.get(source, destination);
    }

    public void addCost(T source, T destination, Object cost) {
        m_costMatrix.put(source, destination, cost);
    }

    public void addCosts(T source, Map<? extends T, ? extends Object> costs) {
        m_costMatrix.put(source, costs);
    }
}
