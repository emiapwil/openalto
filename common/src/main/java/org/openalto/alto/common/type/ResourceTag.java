package org.openalto.alto.common.type;

import java.util.Arrays;

public class ResourceTag {

    private String m_resourceId;
    private String m_tag;

    public ResourceTag(String resourceId, String tag) {
        m_resourceId = resourceId;
        m_tag = tag;
    }

    public String getResourceId() {
        return m_resourceId;
    }

    public String getTag() {
        return m_tag;
    }

    @Override
    public int hashCode() {
        Object data[] = { m_resourceId, m_tag };
        return Arrays.deepHashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof ResourceTag))
            return false;

        ResourceTag that = (ResourceTag)obj;
        Object lhs[] = { this.getResourceId(), this.getTag() };
        Object rhs[] = { that.getResourceId(), that.getTag() };
        return Arrays.deepEquals(lhs, rhs);
    }
}
