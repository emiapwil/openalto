package org.openalto.alto.common.decoder.basic;

import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

import java.net.Inet4Address;
import java.net.Inet6Address;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.type.EndpointAddress;
import org.openalto.alto.common.type.ResourceTag;

import org.openalto.alto.common.decoder.ALTODecoder;
import org.openalto.alto.common.decoder.ALTOChainDecoder;

public class DefaultEndpointPropertyResultDecoder
        extends ALTOChainDecoder<ALTOData<MetaData, DefaultEndpointPropertyResult>> {

    public static final String CATEGORY_ADDR = "addr";
    public static final String CATEGORY_PROP = "prop";
    public static final String CATEGORY_META = "meta";

    public DefaultEndpointPropertyResultDecoder() {
        ResourceTagDecoder rtd = new ResourceTagDecoder();
        CollectionDecoder.CollectionCreator<ResourceTag> creator;
        creator = new CollectionDecoder.CollectionCreator<ResourceTag>() {
            @Override
            public Collection<ResourceTag> create() {
                return new HashSet<ResourceTag>();
            }
        };

        CollectionDecoder<ResourceTag> colDecoder;
        colDecoder = new CollectionDecoder<ResourceTag>(rtd, creator);

        this.add(CATEGORY_META, "dependent-vtags", colDecoder);

        this.add(CATEGORY_ADDR, "ipv4",
                 new InetAddressDecoder("ipv4", Inet4Address.class));
        this.add(CATEGORY_ADDR, "ipv6",
                 new InetAddressDecoder("ipv6", Inet6Address.class));
    }

    @Override
    public ALTOData<MetaData, DefaultEndpointPropertyResult> decode(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(text);
            return this.decode(node);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ALTOData<MetaData, DefaultEndpointPropertyResult> decode(JsonNode node) {
        try {
            MetaData metaData = this.decodeMetaData(node.get("meta"));
            DefaultEndpointPropertyResult map;
            map = this.decodeMap(node.get("endpoint-properties"));

            if (map == null)
                return null;
            return new ALTOData<MetaData, DefaultEndpointPropertyResult>(metaData, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public DefaultEndpointPropertyResult decodeMap(JsonNode mapNode) {
        if ((mapNode == null) || (mapNode.isNull()) || (!mapNode.isObject())) {
            //TODO Raise an exception?
            return null;
        }

        DefaultEndpointPropertyResult map = new DefaultEndpointPropertyResult();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = ((ObjectNode)mapNode).fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            String addr = entry.getKey();
            JsonNode propsNode = entry.getValue();

            if (propsNode == null) {
                //TODO RFC7285 doesn't specify how to deal with empty properties
                continue;
            }

            if (!propsNode.isObject()) {
                //TODO Skip and log the error or raise an exception?
                continue;
            }

            try {
                EndpointAddress<?> endpoint = decodeAddress(addr);
                if (endpoint == null) {
                    continue;
                }

                Map<String, Object> properties = decodeProperties((ObjectNode)propsNode);

                map.addProperties(endpoint, properties);
            } catch (Exception e) {
            }
        }
        return map;
    }

    public EndpointAddress<?> decodeAddress(String compact) {
        String fullAddr[] = compact.split(":", 2);
        if (fullAddr.length != 2)
            return null;

        String family = fullAddr[0];
        String addr = fullAddr[1];

        try {
            ALTODecoder<? extends Object> subDecoder;
            subDecoder = this.get(CATEGORY_ADDR, family);
            if (subDecoder == null)
                return null;

            ALTODecoder<EndpointAddress<?>> addrDecoder;
            addrDecoder = (ALTODecoder<EndpointAddress<?>>)subDecoder;

            return addrDecoder.decode(addr);
        } catch (Exception e) {
        }
        return null;
    }

    public Map<String, Object> decodeProperties(ObjectNode node) {
        Map<String, Object> retval = new HashMap<String, Object>();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = node.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();

            String propName = entry.getKey();
            JsonNode property = entry.getValue();

            try {
                ALTODecoder<?> decoder = this.get(CATEGORY_PROP, propName);

                if (decoder == null) {
                    // See https://tools.ietf.org/html/rfc7285#section-11.4.1.6
                    retval.put(propName, property.asText());
                }

                retval.put(propName, decoder.decode(property));
            } catch (Exception e) {
            }
        }
        return retval;
    }
}
