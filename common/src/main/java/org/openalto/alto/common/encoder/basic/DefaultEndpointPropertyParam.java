package org.openalto.alto.common.encoder.basic;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

import org.openalto.alto.common.type.EndpointAddress;

public class DefaultEndpointPropertyParam {

    private Set<EndpointAddress<?>>
    m_endpoints = new HashSet<EndpointAddress<?>>();

    private Set<String> m_properties = new HashSet<String>();

    public DefaultEndpointPropertyParam() {
    }

    public DefaultEndpointPropertyParam(Collection<EndpointAddress<?>> endpoints,
                                        Collection<String> properties) {
        m_endpoints.addAll(endpoints);
        m_properties.addAll(properties);
    }

    public void addEndpoint(EndpointAddress<?> endpoint) {
        m_endpoints.add(endpoint);
    }

    public void addProperty(String property) {
        m_properties.add(property);
    }

    public Collection<EndpointAddress<?>> getEndpoints() {
        return m_endpoints;
    }

    public Collection<String> getProperties() {
        return m_properties;
    }
}
