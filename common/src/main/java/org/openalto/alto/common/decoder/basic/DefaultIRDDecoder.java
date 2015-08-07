package org.openalto.alto.common.decoder.basic;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import java.net.URISyntaxException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.type.Capability;

import org.openalto.alto.common.resource.ResourceEntry;
import org.openalto.alto.common.resource.ResourceType;
import org.openalto.alto.common.resource.ResourceTypeMapper;

import org.openalto.alto.common.decoder.ALTODecoder;
import org.openalto.alto.common.decoder.ALTOChainDecoder;

public class DefaultIRDDecoder
        extends ALTOChainDecoder<ALTOData<MetaData, List<ResourceEntry>>> {

    private ObjectMapper m_mapper = new ObjectMapper();
    private ResourceTypeMapper m_typeMapper = ResourceTypeMapper.getRFC7285Mapper();

    public DefaultIRDDecoder() {
    }

    @Override
    public ALTOData<MetaData, List<ResourceEntry>> decode(String text) {
        try {
            return this.decode(m_mapper.readTree(text));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ALTOData<MetaData, List<ResourceEntry>> decode(JsonNode node) {
        try {
            List<ResourceEntry> resources = this.decodeResources(node.get("resources"));
            if (resources != null)
                return new ALTOData<MetaData, List<ResourceEntry>>(null, resources);
        } catch (Exception e) {
        }
        return null;
    }

    public List<ResourceEntry> decodeResources(JsonNode resourcesNode) {
        if ((resourcesNode == null) || (resourcesNode.isNull()))
            return null;
        if (!(resourcesNode.isObject()))
            return null;

        List<ResourceEntry> list = new LinkedList<ResourceEntry>();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = ((ObjectNode)resourcesNode).fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> resource = itr.next();
            String rid = resource.getKey();
            JsonNode content = resource.getValue();

            try {
                Map<String, Object> resourceData = new HashMap<String, Object>();
                resourceData = m_mapper.readValue(content.toString(), resourceData.getClass());
                JsonNode paramType = content.get("accepts");
                JsonNode contentType = content.get("media-type");
                String paramTypeName = (paramType == null ? null : paramType.asText());
                String contentTypeName = (contentType == null ? null : contentType.asText());
                ResourceType type = m_typeMapper.getResourceType(paramTypeName, contentTypeName);

                if (type == null)
                    continue;

                ResourceEntry re = new ResourceEntry(content.get("uri").asText(), type);
                //TODO add support for capabilities/uses
                re.setResourceID(rid);

                list.add(re);
            } catch (URISyntaxException e) {
            } catch (Exception e) {
            }
        }
        return list;
    }

    public MetaData decodeMetaData(ObjectNode metaNode) {
        MetaData meta = new MetaData();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = metaNode.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            //TODO
        }
        return meta;
    }

    public Set<Capability<?>> decodeCapabilities(ObjectNode capabilities) {
        Set<Capability<?>> retval = new HashSet<Capability<?>>();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = capabilities.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            String capabilityName = entry.getKey();
            JsonNode capabilityValue = entry.getValue();

            ALTODecoder<Set<? extends Capability<?>>> decoder;
            Set<? extends Capability<?>> newCapbilities;
            try {
                ALTOChainDecoder<Set<? extends Capability<?>>> capDecoder;
                capDecoder = (ALTOChainDecoder<Set<? extends Capability<?>>>)this.get("capability");
                decoder = (ALTODecoder<Set<? extends Capability<?>>>)capDecoder.get(capabilityName);
                newCapbilities = decoder.decode(capabilityValue);
                retval.addAll(newCapbilities);
            } catch (Exception e) {
            }
        }

        return retval;
    }

    public Set<ResourceEntry> decodeUses(ArrayNode uses) {
        //TODO
        return null;
    }
}
