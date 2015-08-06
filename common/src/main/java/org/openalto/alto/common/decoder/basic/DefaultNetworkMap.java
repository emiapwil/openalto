package org.openalto.alto.common.decoder.basic;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

import org.openalto.alto.common.type.EndpointAddress;

public class DefaultNetworkMap {

    private Map<String, Set<EndpointAddress<?>>>
    m_map = new HashMap<String, Set<EndpointAddress<?>>>();

    public DefaultNetworkMap() {
    }

    public DefaultNetworkMap(Map<String, Set<EndpointAddress<?>>> map) {
        m_map = new HashMap<String, Set<EndpointAddress<?>>>(map);
    }

    public Map<String, Set<EndpointAddress<?>>> getNodes() {
        return m_map;
    }

    public void addNode(String nodeId, Set<EndpointAddress<?>> nodeAddress) {
        m_map.put(nodeId, nodeAddress);
    }

    public void addNodeAddress(String nodeId, EndpointAddress<?> addr) {
        if (!m_map.containsKey(nodeId)) {
            m_map.put(nodeId, new HashSet<EndpointAddress<?>>());
        }
        m_map.get(nodeId).add(addr);
    }

    public Set<EndpointAddress<?>> getNode(String nodeId) {
        return m_map.get(nodeId);
    }

    public void removeNode(String nodeId) {
        m_map.remove(nodeId);
    }

    public void removeNodeAddress(String nodeId, EndpointAddress<?> addr) {
        if (!m_map.containsKey(nodeId)) {
            return;
        }
        m_map.get(nodeId).remove(addr);
    }
}
