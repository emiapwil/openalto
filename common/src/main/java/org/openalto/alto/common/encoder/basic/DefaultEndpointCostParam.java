package org.openalto.alto.common.encoder.basic;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.type.EndpointAddress;

public class DefaultEndpointCostParam {

    private CostType m_type;

    private List<String>
    m_constraints = new LinkedList<String>();

    private Set<EndpointAddress<?>>
    m_srcHosts = new HashSet<EndpointAddress<?>>();

    private Set<EndpointAddress<?>>
    m_dstHosts = new HashSet<EndpointAddress<?>>();

    public DefaultEndpointCostParam(CostType type) {
        m_type = type;
    }

    public CostType getCostType() {
        return m_type;
    }

    public DefaultEndpointCostParam addConstraint(String constraint) {
        m_constraints.add(constraint);
        return this;
    }

    public DefaultEndpointCostParam addSource(EndpointAddress<?> src) {
        m_srcHosts.add(src);
        return this;
    }

    public DefaultEndpointCostParam addDestination(EndpointAddress<?> dst) {
        m_dstHosts.add(dst);
        return this;
    }

    public List<String> getConstraints() {
        return m_constraints;
    }

    public Set<EndpointAddress<?>> getSources() {
        return m_srcHosts;
    }

    public Set<EndpointAddress<?>> getDestinations() {
        return m_dstHosts;
    }
}
