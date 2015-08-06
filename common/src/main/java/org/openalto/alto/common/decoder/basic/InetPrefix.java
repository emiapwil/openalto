package org.openalto.alto.common.decoder.basic;

import java.net.InetAddress;

import java.util.Arrays;

public class InetPrefix {

    private InetAddress m_addr;
    private int m_prefixLen;

    public InetPrefix(InetAddress addr, int prefixLen) {
        m_addr = addr;
        m_prefixLen = prefixLen;
    }

    public InetAddress getAddress() {
        return m_addr;
    }

    public int getPrefixLength() {
        return m_prefixLen;
    }

    @Override
    public int hashCode() {
        Object[] data = { new Integer(m_prefixLen), m_addr };
        return Arrays.deepHashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof InetPrefix))
            return false;

        InetPrefix that = (InetPrefix)obj;
        Object[] lhs = { new Integer(this.getPrefixLength()), this.getAddress() };
        Object[] rhs = { new Integer(that.getPrefixLength()), that.getAddress() };
        return Arrays.deepEquals(lhs, rhs);
    }
}
