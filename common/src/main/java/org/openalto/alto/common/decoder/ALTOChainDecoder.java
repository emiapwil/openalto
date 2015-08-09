package org.openalto.alto.common.decoder;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public abstract class ALTOChainDecoder<T> implements ALTODecoder<T> {

    private Map<String, Map<Object, ALTODecoder<? extends Object>>>
    m_subDecoder = new HashMap<String, Map<Object, ALTODecoder<? extends Object>>>();

    public ALTOChainDecoder add(String category, Object field,
                                ALTODecoder<? extends Object> decoder) {
        if (!m_subDecoder.containsKey(category)) {
            m_subDecoder.put(category, new HashMap<Object, ALTODecoder<? extends Object>>());
        }
        Map<Object, ALTODecoder<? extends Object>> categoried;
        categoried = m_subDecoder.get(category);

        categoried.put(field, decoder);
        return this;
    }

    public ALTODecoder<?> get(String category, Object field) {
        Map<Object, ALTODecoder<? extends Object>> categoried;
        categoried = m_subDecoder.get(category);
        if (categoried == null)
            return null;
        return categoried.get(field);
    }

    public Collection<Map.Entry<Object, ALTODecoder<? extends Object>>> getAll(String category) {
        Map<Object, ALTODecoder<? extends Object>> categoried;
        categoried = m_subDecoder.get(category);
        if (categoried == null)
            return null;

        return categoried.entrySet();
    }

    public ALTOChainDecoder remove(String category, Object field) {
        Map<Object, ALTODecoder<? extends Object>> categoried;
        categoried = m_subDecoder.get(category);
        if (categoried != null) {
            categoried.remove(field);
        }
        return this;
    }
}
