package org.openalto.alto.common.resource;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import javax.ws.rs.core.MediaType;

import org.openalto.alto.common.ALTOMediaType;

public class ResourceTypeMapper {

    public static class MediaTypePair {

        private MediaType m_paramType;
        private MediaType m_contentType;

        public MediaTypePair(MediaType paramType, MediaType contentType) {
            m_paramType = paramType;
            m_contentType = contentType;
        }

        public MediaType getParamType() {
            return m_paramType;
        }

        public MediaType getContentType() {
            return m_contentType;
        }

        @Override
        public int hashCode() {
            MediaType[] lhs = { m_paramType, m_contentType };
            return Arrays.deepHashCode(lhs);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof MediaTypePair))
                return false;
            MediaTypePair _obj = (MediaTypePair)obj;
            MediaType[] lhs = { m_paramType, m_contentType };
            MediaType[] rhs = { _obj.getParamType(), _obj.getContentType() };
            return Arrays.deepEquals(lhs, rhs);
        }
    }

    private Map<ResourceType, MediaTypePair>
    m_mapping = new HashMap<ResourceType, MediaTypePair>();

    private Map<MediaTypePair, ResourceType>
    m_revmapping = new HashMap<MediaTypePair, ResourceType>();

    public boolean addResourceType(ResourceType t,
                                MediaType paramType,
                                MediaType contentType,
                                boolean force) {
        MediaTypePair pair = new MediaTypePair(paramType, contentType);
        if (m_mapping.containsKey(t) || m_revmapping.containsKey(pair)) {
            if (!force)
                return false;
            m_mapping.remove(t);
            m_revmapping.remove(pair);
        }
        m_mapping.put(t, pair);
        m_revmapping.put(pair, t);
        return true;
    }

    public MediaTypePair get(ResourceType t) {
        return m_mapping.get(t);
    }

    public MediaTypePair get(String typeName) {
        return this.get(new ResourceType(typeName));
    }

    public MediaType getParamType(ResourceType t) {
        MediaTypePair pair = this.get(t);
        return (pair == null ? null : pair.getParamType());
    }

    public MediaType getParamType(String typeName) {
        return getParamType(new ResourceType(typeName));
    }

    public MediaType getContentType(ResourceType t) {
        MediaTypePair pair = this.get(t);
        return (pair == null ? null : pair.getContentType());
    }

    public MediaType getContentType(String typeName) {
        return getContentType(new ResourceType(typeName));
    }

    public ResourceType getResourceType(MediaType paramType,
                                        MediaType contentType) {
        MediaTypePair pair = new MediaTypePair(paramType, contentType);
        return m_revmapping.get(pair);
    }

    public ResourceType getResourceType(String paraTypeName,
                                        String contentTypeName) {
        MediaType paramType = (paraTypeName == null ? null
                                    : MediaType.valueOf(paraTypeName));
        MediaType contentType = (contentTypeName == null ? null
                                    : MediaType.valueOf(contentTypeName));
        return getResourceType(paramType, contentType);
    }

    public void removeResourceType(ResourceType t) {
        if (!m_mapping.containsKey(t))
            return;
        MediaTypePair pair = m_mapping.get(t);
        m_mapping.remove(t);
        m_revmapping.remove(pair);
    }

    public static final Object DEFAULT_MAPPING[][] = {
        {
            ResourceType.DIRECTORY_TYPE,
            null,
            ALTOMediaType.DIRECTORY_TYPE
        },
        {
            ResourceType.NETWORK_MAP_TYPE,
            null,
            ALTOMediaType.NETWORK_MAP_TYPE
        },
        {
            ResourceType.FILTERED_NETWORK_MAP_TYPE,
            ALTOMediaType.NETWORK_MAP_FILTER_TYPE,
            ALTOMediaType.NETWORK_MAP_TYPE
        },
        {
            ResourceType.COST_MAP_TYPE,
            null,
            ALTOMediaType.COST_MAP_TYPE
        },
        {
            ResourceType.FILTERED_COST_MAP_TYPE,
            ALTOMediaType.COST_MAP_FILTER_TYPE,
            ALTOMediaType.COST_MAP_TYPE
        },
        {
            ResourceType.ENDPOINT_COST_SERVICE_TYPE,
            ALTOMediaType.ENDPOINT_COST_PARAMS_TYPE,
            ALTOMediaType.ENDPOINT_COST_TYPE
        },
        {
            ResourceType.ENDPOINT_PROP_SERVICE_TYPE,
            ALTOMediaType.ENDPOINT_PROP_PARAMS_TYPE,
            ALTOMediaType.ENDPOINT_PROP_TYPE
        }
    };

    public static ResourceTypeMapper getRFC7285Mapper() {
        ResourceTypeMapper mapper = new ResourceTypeMapper();
        for (Object[] mapping: DEFAULT_MAPPING) {
            ResourceType resourceType = (ResourceType)mapping[0];
            MediaType paramType = (MediaType)mapping[1];
            MediaType contentType = (MediaType)mapping[2];
            mapper.addResourceType(resourceType, paramType, contentType, false);
        }
        return mapper;
    }
}
