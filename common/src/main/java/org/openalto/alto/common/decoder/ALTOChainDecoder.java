package org.openalto.alto.common.decoder;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public abstract class ALTOChainDecoder<T> implements ALTODecoder<T> {

    private Map<String, ALTODecoder<? extends Object>>
    m_subDecoder = new HashMap<String, ALTODecoder<? extends Object>>();

    public ALTOChainDecoder add(String field,
                                ALTODecoder<? extends Object> decoder) {
        m_subDecoder.put(field, decoder);
        return this;
    }

    public ALTODecoder<?> get(String field) {
        return m_subDecoder.get(field);
    }

    public Collection<ALTODecoder<?>> getAll() {
        return m_subDecoder.values();
    }

    public ALTOChainDecoder remove(String field) {
        m_subDecoder.remove(field);
        return this;
    }
}
