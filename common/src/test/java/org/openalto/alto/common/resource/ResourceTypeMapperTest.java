package org.openalto.alto.common.resource;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;
import static org.openalto.alto.common.resource.ResourceTypeMapper.MediaTypePair;

public class ResourceTypeMapperTest {

    @Test
    public void testResourceTypeMapper() {
        ResourceTypeMapper mapper = ResourceTypeMapper.getRFC7285Mapper();
        for (Object[] mapping: ResourceTypeMapper.DEFAULT_MAPPING) {
            ResourceType resourceType = (ResourceType)mapping[0];
            MediaType paramType = (MediaType)mapping[1];
            String paramTypeName = (paramType == null ? null: paramType.toString());
            MediaType contentType = (MediaType)mapping[2];
            String contentTypeName = (contentType == null ? null: contentType.toString());
            MediaTypePair mtp = new MediaTypePair(paramType, contentType);

            assertEquals(mapper.get(resourceType), mtp);
            assertEquals(mapper.get(resourceType.toString()), mtp);
            assertEquals(mapper.get(resourceType).getParamType(), paramType);
            assertEquals(mapper.get(resourceType).getContentType(), contentType);

            assertEquals(mapper.getParamType(resourceType), paramType);
            assertEquals(mapper.getParamType(resourceType.toString()), paramType);

            assertEquals(mapper.getContentType(resourceType), contentType);
            assertEquals(mapper.getContentType(resourceType.toString()), contentType);

            assertEquals(mapper.getResourceType(paramType, contentType), resourceType);
            assertEquals(mapper.getResourceType(paramTypeName, contentTypeName), resourceType);
        }
    }

}
