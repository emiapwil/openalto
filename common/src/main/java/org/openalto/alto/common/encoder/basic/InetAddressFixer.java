package org.openalto.alto.common.encoder.basic;

import java.net.InetAddress;

public class InetAddressFixer {

    private InetAddress m_addr;

    public InetAddressFixer(InetAddress addr) {
        m_addr = addr;
    }

    public InetAddress getAddress() {
        return m_addr;
    }

    @Override
    public String toString() {
        return getAddress().getHostAddress();
    }
}
