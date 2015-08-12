package org.openalto.alto.common.encoder.basic;

import java.net.InetAddress;

import org.openalto.alto.common.type.EndpointAddress;

public class InetAddressFixer extends EndpointAddress<InetAddress> {

    public InetAddressFixer(String family, InetAddress addr) {
        super(family, addr);
    }

    @Override
    public String toString() {
        return this.getFamily() + ":" + this.getAddr().getHostAddress();
    }
}
