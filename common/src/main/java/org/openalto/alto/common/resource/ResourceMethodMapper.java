package org.openalto.alto.common.resource;

import java.util.Map;
import java.util.HashMap;

public class ResourceMethodMapper {

    private Map<ResourceType, String>
    m_mapping = new HashMap<ResourceType, String>();

    public ResourceMethodMapper() {
    }

    public void add(ResourceType type, String method) {
        m_mapping.put(type, method);
    }

    public String get(ResourceType type) {
        return m_mapping.get(type);
    }

    public void remove(ResourceType type) {
        m_mapping.remove(type);
    }

    public static final Object DEFAULT_MAPPING[][] = {
        {
            ResourceType.DIRECTORY_TYPE,
            "GET"
        },
        {
            ResourceType.NETWORK_MAP_TYPE,
            "GET"
        },
        {
            ResourceType.FILTERED_NETWORK_MAP_TYPE,
            "POST"
        },
        {
            ResourceType.COST_MAP_TYPE,
            "GET"
        },
        {
            ResourceType.FILTERED_COST_MAP_TYPE,
            "POST"
        },
        {
            ResourceType.ENDPOINT_COST_SERVICE_TYPE,
            "POST"
        },
        {
            ResourceType.ENDPOINT_PROP_SERVICE_TYPE,
            "POST"
        }
    };

    public static ResourceMethodMapper getRFC7285Mapper() {
        ResourceMethodMapper mapper = new ResourceMethodMapper();
        for (Object[] mapping: DEFAULT_MAPPING) {
            ResourceType resourceType = (ResourceType)mapping[0];
            String method = (String)mapping[1];
            mapper.add(resourceType, method);
        }
        return mapper;
    }
}
