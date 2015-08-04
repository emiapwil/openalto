package org.openalto.alto.client.wrapper.ird;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import java.net.URISyntaxException;

import javax.ws.rs.core.Response;

import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

import org.openalto.alto.common.ALTOMediaType;
import org.openalto.alto.common.resource.ResourceEntry;
import org.openalto.alto.common.resource.ResourceType;
import org.openalto.alto.common.resource.ResourceTypeMapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IRDResponseParser extends ALTOResponseParser {

    @Override
    public ALTOResponse parse(Response response) {
        if (response == null) {
            System.out.println("response is null");
            return null;
        }

        if (!success(response.getStatusInfo())) {
            System.out.println("response is failed: " + response.getStatusInfo());
            return null;
        }

        if (!response.getMediaType().equals(ALTOMediaType.DIRECTORY_TYPE)) {
            System.out.println("wrong media type: " + response.getMediaType());
            return null;
        }

        ResourceTypeMapper rtm = ResourceTypeMapper.getRFC7285Mapper();
        List<ResourceEntry> list = new LinkedList<ResourceEntry>();

        String data = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(data);
        } catch (IOException e) {
            System.out.println("Failed to parse the data");
            return null;
        }
        JsonNode resources = node.get("resources");
        for (Iterator<Map.Entry<String, JsonNode>> itr = resources.fields(); itr.hasNext();) {
            Map.Entry<String, JsonNode> resource = itr.next();
            String rid = resource.getKey();
            JsonNode content = resource.getValue();

            try {
                JsonNode paramType = content.get("accepts");
                JsonNode contentType = content.get("media-type");
                String paramTypeName = (paramType == null ? null : paramType.asText());
                String contentTypeName = (contentType == null ? null : contentType.asText());
                ResourceType type = rtm.getResourceType(paramTypeName, contentTypeName);

                if (type == null)
                    continue;

                ResourceEntry re = new ResourceEntry(content.get("uri").asText(), type);
                list.add(re.setResourceID(rid));
            } catch (URISyntaxException e) {
            } catch (Exception e) {
            }
        }

        //TODO
        return new ALTOResponseBase(false, response, list);
    }
}
