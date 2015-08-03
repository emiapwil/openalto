package org.openalto.alto.common.resource;

public class ResourceType {

    private String m_type;

    public ResourceType(String type) {
        m_type = type;
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof ResourceType) {
            return this.toString().equals(obj.toString());
        }
        return false;
    }

    public String toString() {
        if (m_type == null)
            return "";
        return m_type;
    }
}
