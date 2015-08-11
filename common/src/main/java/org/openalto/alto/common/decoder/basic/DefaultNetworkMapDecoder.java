package org.openalto.alto.common.decoder.basic;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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

import org.openalto.alto.common.decoder.ALTODecoder;
import org.openalto.alto.common.decoder.ALTOChainDecoder;

import org.openalto.alto.common.standard.RFC7285;

public class DefaultNetworkMapDecoder
        extends ALTOChainDecoder<ALTOData<MetaData, DefaultNetworkMap>> {

    public static final String CATEGORY_ADDR = "addr";
    public static final String CATEGORY_META = "meta";

    public static final int IPV4_MAX_BITS = 32;

    public static final int IPV6_MAX_BITS = 128;

    public DefaultNetworkMapDecoder() {
        this.add(CATEGORY_META, RFC7285.META_FIELD_VTAG, new ResourceTagDecoder());

        ALTODecoder<?> ipv4Decoder;
        ipv4Decoder = new InetSubnetDecoder(RFC7285.ADDR_FAMILY_IPV4,
                                            IPV4_MAX_BITS, Inet4Address.class);
        this.add(CATEGORY_ADDR, RFC7285.ADDR_FAMILY_IPV4, ipv4Decoder);

        ALTODecoder<?> ipv6Decoder;
        ipv6Decoder = new InetSubnetDecoder(RFC7285.ADDR_FAMILY_IPV6,
                                            IPV6_MAX_BITS, Inet6Address.class);
        this.add(CATEGORY_ADDR, RFC7285.ADDR_FAMILY_IPV6, ipv6Decoder);
    }

    @Override
    public ALTOData<MetaData, DefaultNetworkMap> decode(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(text);
            return this.decode(node);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ALTOData<MetaData, DefaultNetworkMap> decode(JsonNode node) {
        try {
            MetaData metaData;
            metaData = this.decodeMetaData(node.get(RFC7285.META_FIELD));

            DefaultNetworkMap map;
            map = this.decodeMap(node.get(RFC7285.NETWORK_MAP_FIELD));
            if (map == null)
                return null;

            return new ALTOData<MetaData, DefaultNetworkMap>(metaData, map);
        } catch (Exception e) {
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

    public DefaultNetworkMap decodeMap(JsonNode mapNode) {
        if ((mapNode == null) || (mapNode.isNull()) || (!mapNode.isObject())) {
            //TODO Raise an exception?
            return null;
        }

        DefaultNetworkMap map = new DefaultNetworkMap();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = ((ObjectNode)mapNode).fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            String pid = entry.getKey();
            JsonNode addrGroup = entry.getValue();

            if ((addrGroup == null) || (addrGroup.isNull())) {
                /*
                 * See https://tools.ietf.org/html/rfc7285#section-11.2.1.7
                 * */
                map.addNode(pid, new HashSet<EndpointAddress<?>>());
                continue;
            }

            if (!addrGroup.isObject()) {
                //TODO Skip and log the error or raise an exception?
                continue;
            }

            try {
                Set<EndpointAddress<?>> endpoint = decodeEndpoint((ObjectNode)addrGroup);
                if (endpoint != null)
                    map.addNode(pid, endpoint);
            } catch (Exception e) {
            }
        }
        return map;
    }

    public Set<EndpointAddress<?>> decodeEndpoint(ObjectNode endpoint) {
        Set<EndpointAddress<?>> retval = new HashSet<EndpointAddress<?>>();

        Iterator<Map.Entry<String, JsonNode>> itr;
        for (itr = endpoint.fields(); itr.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = itr.next();
            String family = entry.getKey();
            JsonNode addrList = entry.getValue();

            ALTODecoder<EndpointAddress<?>> addrDecoder;
            try {
                /* See https://tools.ietf.org/html/rfc7285#section-11.2.1.7 */

                ALTODecoder<? extends Object> subDecoder;
                subDecoder = this.get(CATEGORY_ADDR, family);
                if (subDecoder == null) {
                    continue;
                }

                addrDecoder = (ALTODecoder<EndpointAddress<?>>)subDecoder;
            } catch (Exception e) {
                continue;
            }

            if ((addrList == null) || (!addrList.isArray())) {
                //TODO Skip or raise?
                continue;
            }

            Iterator<JsonNode> addrItr;
            for (addrItr = addrList.elements(); addrItr.hasNext(); ) {
                JsonNode addr = addrItr.next();

                if (addr == null) {
                    //TODO Skip and log the error or raise an exception?
                    continue;
                }
                EndpointAddress<?> parsedAddr = addrDecoder.decode(addr);
                if (parsedAddr != null)
                    retval.add(parsedAddr);
            }
        }
        return retval;
    }
}
