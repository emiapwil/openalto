package org.openalto.alto.common.encoder.basic;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

public class DefaultNetworkMapFilter {

    private Set<String> m_pidNames = new HashSet<String>();
    private Set<String> m_addrTypes = new HashSet<String>();

    public DefaultNetworkMapFilter() {
    }

    public DefaultNetworkMapFilter(Collection<String> pidNames,
                                   Collection<String> addrTypes) {
        m_pidNames.addAll(pidNames);
        m_addrTypes.addAll(addrTypes);
    }

    public void addPid(String pidName) {
        m_pidNames.add(pidName);
    }

    public void addAddrType(String addrType) {
        m_addrTypes.add(addrType);
    }

    public Collection<String> getPidNames() {
        return m_pidNames;
    }

    public Collection<String> getAddrTypes() {
        return m_addrTypes;
    }
}
