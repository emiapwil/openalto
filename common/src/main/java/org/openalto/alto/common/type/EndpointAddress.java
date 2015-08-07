package org.openalto.alto.common.type;

import java.util.Arrays;

public class EndpointAddress<T> {

    private String m_addrFamily;
    private T m_addr;

    public EndpointAddress(String addrFamily, T addr) {
        m_addrFamily = addrFamily;
        m_addr = addr;
    }

    public String getFamily() {
        return m_addrFamily;
    }

    public T getAddr() {
        return m_addr;
    }

    @Override
    public int hashCode() {
        Object[] data = { this.getFamily(), this.getAddr() };
        return Arrays.deepHashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!this.getClass().isInstance(obj))
            return false;

        EndpointAddress<? extends T> that = (EndpointAddress<? extends T>)obj;
        Object[] lhs = { this.getFamily(), this.getAddr() };
        Object[] rhs = { that.getFamily(), that.getAddr() };
        return Arrays.deepEquals(lhs, rhs);
    }

    @Override
    public String toString() {
        return getFamily() + ":" + getAddr().toString();
    }
}
