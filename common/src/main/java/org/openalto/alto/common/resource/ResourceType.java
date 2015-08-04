package org.openalto.alto.common.resource;

public class ResourceType {

    public static final String
    DIRECTORY = "directory";

    public static final ResourceType
    DIRECTORY_TYPE = new ResourceType(DIRECTORY);

    public static final String
    NETWORK_MAP = "network-map";

    public static final ResourceType
    NETWORK_MAP_TYPE = new ResourceType(NETWORK_MAP);

    public static final String
    FILTERED_NETWORK_MAP = "filtered-network-map";

    public static final ResourceType
    FILTERED_NETWORK_MAP_TYPE = new ResourceType(FILTERED_NETWORK_MAP);

    public static final String
    COST_MAP = "cost-map";

    public static final ResourceType
    COST_MAP_TYPE = new ResourceType(COST_MAP);

    public static final String
    FILTERED_COST_MAP = "filtered-cost-map";

    public static final ResourceType
    FILTERED_COST_MAP_TYPE = new ResourceType(FILTERED_COST_MAP);

    public static final String
    ENDPOINT_COST_SERVICE = "endpoint-cost-service";

    public static final ResourceType
    ENDPOINT_COST_SERVICE_TYPE = new ResourceType(ENDPOINT_COST_SERVICE);

    public static final String
    ENDPOINT_PROP_SERVICE = "endpoint-prop-service";

    public static final ResourceType
    ENDPOINT_PROP_SERVICE_TYPE = new ResourceType(ENDPOINT_PROP_SERVICE);

    private String m_type;

    public ResourceType(String type) {
        m_type = type;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return this.toString().equals(obj.toString());
    }

    public String toString() {
        if (m_type == null)
            return "";
        return m_type;
    }
}
