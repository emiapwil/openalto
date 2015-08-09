package org.openalto.alto.common.encoder.basic;

import java.net.InetAddress;
import java.util.Arrays;

public class InetAddressFixer {

    private InetAddress m_addr;

    public InetAddressFixer(InetAddress addr) {
        m_addr = addr;
    }

    public InetAddress getAddress() {
        return m_addr;
    }

    @Override
    public int hashCode() {
        if (m_addr == null)
            return 0;
        return m_addr.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof InetAddressFixer) {
            InetAddressFixer that = (InetAddressFixer)obj;
            Object lhs[] = { this.getAddress() };
            Object rhs[] = { that.getAddress() };
            return Arrays.equals(lhs, rhs);
        }
        if (obj instanceof InetAddress) {
            Object lhs[] = { this.getAddress() };
            Object rhs[] = { obj };
            return Arrays.equals(lhs, rhs);
        }
        return false;
    }

    @Override
    public String toString() {
        return getAddress().getHostAddress();
    }
}
