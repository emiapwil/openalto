package org.openalto.alto.common.decoder.basic;

import java.util.Map;

import org.openalto.alto.common.type.EndpointAddress;
import org.openalto.alto.common.type.Map2D;

public class DefaultEndpointPropertyResult {

    private Map2D<EndpointAddress<?>, String, Object>
    m_propMap = new Map2D<EndpointAddress<?>, String, Object>();

    public DefaultEndpointPropertyResult() {
    }

    public DefaultEndpointPropertyResult(Map2D<? extends EndpointAddress<?>, String, ? extends Object> propMap) {
        m_propMap = new Map2D(propMap.getFullMap());
    }

    public Map<EndpointAddress<?>, Map<String, Object>> getPropertyMap() {
        return m_propMap.getFullMap();
    }

    public void addProperty(EndpointAddress<?> addr, String propName, Object data) {
        m_propMap.put(addr, propName, data);
    }

    public void addProperties(EndpointAddress<?> addr, Map<String, Object> properties) {
        m_propMap.put(addr, properties);
    }

    public Map<String, Object> getProperties(EndpointAddress<?> addr) {
        return m_propMap.get(addr);
    }

    public Object getProperty(EndpointAddress<?> addr, String propName) {
        return m_propMap.get(addr, propName);
    }

}
