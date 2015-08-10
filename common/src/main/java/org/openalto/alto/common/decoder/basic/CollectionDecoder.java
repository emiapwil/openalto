package org.openalto.alto.common.decoder.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.openalto.alto.common.decoder.ALTODecoder;

import java.util.Iterator;
import java.util.Collection;

public class CollectionDecoder<T>
        implements ALTODecoder<Collection<T>> {

    public static abstract class CollectionCreator<T> {
        public abstract Collection<T> create();
    }

    private ALTODecoder<T> m_decoder;
    private CollectionCreator m_creator;

    public CollectionDecoder(ALTODecoder<T> decoder,
                             CollectionCreator<T> creator) {
        m_decoder = decoder;
        m_creator = creator;
    }

    @Override
    public Collection<T> decode(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return decode(mapper.readTree(text));
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Collection<T> decode(JsonNode node) {
        if ((node == null) || (!node.isArray()))
            return null;

        Collection<T> retval = null;
        try {
            retval = m_creator.create();
        } catch (Exception e) {
            return null;
        }

        ArrayNode listNode = (ArrayNode)node;
        Iterator<JsonNode> itr;
        for (itr = listNode.elements(); itr.hasNext(); ) {
            JsonNode dataNode = itr.next();
            try {
                T decoded = m_decoder.decode(dataNode);
                if (decoded == null)
                    continue;

                retval.add(decoded);
            } catch (Exception e) {
            }
        }
        return retval;
    }
}
