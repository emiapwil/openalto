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
import org.openalto.alto.common.type.ALTOMetaData;
import org.openalto.alto.common.type.EndpointAddress;

import org.openalto.alto.common.decoder.ALTODecoder;
import org.openalto.alto.common.decoder.ALTOChainDecoder;

public class DefaultNetworkMapDecoder
        extends ALTOChainDecoder<ALTOData<ALTOMetaData, DefaultNetworkMap>> {

    public DefaultNetworkMapDecoder() {
        this.add("ipv4", new InetSubnetDecoder("ipv4", 32, Inet4Address.class));
        this.add("ipv6", new InetSubnetDecoder("ipv6", 128, Inet6Address.class));
    }

    @Override
    public ALTOData<ALTOMetaData, DefaultNetworkMap> decode(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(text);
            return this.decode(node);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ALTOData<ALTOMetaData, DefaultNetworkMap> decode(JsonNode node) {
        try {
            DefaultNetworkMap map = this.decodeMap(node.get("network-map"));
            if (map != null) {
                return new ALTOData<ALTOMetaData, DefaultNetworkMap>(null, map);
            }
        } catch (Exception e) {
        }
        return null;
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

                ALTODecoder<? extends Object> subDecoder = this.get(family);
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
