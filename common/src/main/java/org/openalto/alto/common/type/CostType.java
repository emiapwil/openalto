package org.openalto.alto.common.type;

import java.util.Arrays;

public class CostType {

    private String m_mode;
    private String m_metric;

    public CostType(String mode, String metric) {
        m_mode = mode;
        m_metric = metric;
    }

    public String getMode() {
        return m_mode;
    }

    public String getMetric() {
        return m_metric;
    }

    @Override
    public int hashCode() {
        Object data[] = { m_mode, m_metric };
        return Arrays.deepHashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof CostType))
            return false;

        CostType that = (CostType)obj;
        Object lhs[] = { this.getMode(), this.getMetric() };
        Object rhs[] = { that.getMode(), that.getMetric() };
        return Arrays.deepEquals(lhs, rhs);
    }
}
