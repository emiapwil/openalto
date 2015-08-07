package org.openalto.alto.common.type;

import java.util.Arrays;

public class Capability<T> {

    private String m_category;

    private T m_spec;

    public Capability(String category, T spec) {
        m_category = category;
        m_spec = spec;
    }

    public String getCategory() {
        return m_category;
    }

    public T getSpec() {
        return m_spec;
    }

    @Override
    public int hashCode() {
        Object data[] = { m_category, m_spec };
        return Arrays.deepHashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!this.getClass().isInstance(obj))
            return false;
        Capability<? extends T> that = (Capability<? extends T>)obj;
        Object lhs[] = { this.getCategory(), this.getSpec() };
        Object rhs[] = { that.getCategory(), that.getSpec() };
        return Arrays.deepEquals(lhs, rhs);
    }
}
