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

    public static final String CATEGORY_CAPABILITY = "capability";
    public static final String CATEGORY_META = "meta";

    private ObjectMapper m_mapper = new ObjectMapper();
    private ResourceTypeMapper m_typeMapper = ResourceTypeMapper.getRFC7285Mapper();

    public DefaultIRDDecoder() {
        CostCapabilityDecoder ccDecoder = new CostCapabilityDecoder();
        this.add(CATEGORY_META, "cost-types", ccDecoder.metaDecoder());
        this.add(CATEGORY_CAPABILITY, "cost-type-names", ccDecoder.capabilityDecoder());
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
            MetaData meta = this.decodeMetaData(node.get("meta"));
            List<ResourceEntry> resources = this.decodeResources(node.get("resources"));
            if (resources != null)
                return new ALTOData<MetaData, List<ResourceEntry>>(meta, resources);
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

                Set<Capability<?>> capabilities = new HashSet<Capability<?>>();
                JsonNode capabilitiesNode = content.get("capabilities");
                if (capabilitiesNode != null) {
                    capabilities.addAll(this.decodeCapabilities(capabilitiesNode));
                }

                if (type == null)
                    continue;

                ResourceEntry re = new ResourceEntry(content.get("uri").asText(), type);
                //TODO add support for capabilities/uses
                re.setResourceID(rid);
                re.setCapabilities(capabilities);

                list.add(re);
            } catch (URISyntaxException e) {
            } catch (Exception e) {
            }
        }
        return list;
    }

    public MetaData decodeMetaData(JsonNode node) {
        if ((node == null) || (!node.isObject()))
            return null;
        ObjectNode metaNode = (ObjectNode)node;
        MetaData meta = new MetaData();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = metaNode.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            String field = entry.getKey();
            JsonNode target = entry.getValue();

            ALTODecoder<?> decoder = this.get(CATEGORY_META, field);
            if (decoder == null) {
                meta.put(field, target.toString());
                continue;
            }

            Object decoded = decoder.decode(target);
            if (decoded == null) {
                continue;
            }

            meta.put(field, decoded);
        }

        return meta;
    }

    public Set<Capability<?>> decodeCapabilities(JsonNode node) {
        if ((node == null) || (!node.isObject()))
            return null;
        ObjectNode capabilitiesNode = (ObjectNode)node;
        Set<Capability<?>> capabilities = new HashSet<Capability<?>>();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = capabilitiesNode.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            String capabilityName = entry.getKey();
            JsonNode capabilityValue = entry.getValue();

            ALTODecoder<Set<? extends Capability<?>>> decoder;
            try {
                ALTODecoder<? extends Object> subDecoder;
                subDecoder = this.get(CATEGORY_CAPABILITY, capabilityName);

                decoder = (ALTODecoder<Set<? extends Capability<?>>>)subDecoder;

                Set<? extends Capability<?>> newCapabilities;
                newCapabilities = decoder.decode(capabilityValue);

                capabilities.addAll(newCapabilities);
            } catch (Exception e) {
            }
        }

        return capabilities;
    }

    public Set<ResourceEntry> decodeUses(ArrayNode uses) {
        //TODO
        return null;
    }
}
