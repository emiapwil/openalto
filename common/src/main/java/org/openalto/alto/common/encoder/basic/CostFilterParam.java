package org.openalto.alto.common.encoder.basic;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

import org.openalto.alto.common.type.CostType;

public class CostFilterParam<T> {

    private CostType m_type;

    private List<String>
    m_constraints = new LinkedList<String>();

    private Set<T> m_srcHosts = new HashSet<T>();

    private Set<T>
    m_dstHosts = new HashSet<T>();

    public CostFilterParam(CostType type) {
        m_type = type;
    }

    public CostType getCostType() {
        return m_type;
    }

    public CostFilterParam<T> addConstraint(String constraint) {
        m_constraints.add(constraint);
        return this;
    }

    public CostFilterParam<T> addSource(T src) {
        m_srcHosts.add(src);
        return this;
    }

    public CostFilterParam<T> addDestination(T dst) {
        m_dstHosts.add(dst);
        return this;
    }

    public List<String> getConstraints() {
        return m_constraints;
    }

    public Set<T> getSources() {
        return m_srcHosts;
    }

    public Set<T> getDestinations() {
        return m_dstHosts;
    }
}
