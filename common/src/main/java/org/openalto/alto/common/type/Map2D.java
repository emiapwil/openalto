package org.openalto.alto.common.type;

import java.util.Map;
import java.util.HashMap;

public class Map2D<K1, K2, E> {

    private Map<K1, Map<K2, E>> m_map = new HashMap<K1, Map<K2, E>>();

    public Map2D() {
    }

    public Map2D(Map<? extends K1, Map<? extends K2, ? extends E>> rhs) {
        m_map = new HashMap<K1, Map<K2, E>>();
        for (K1 key1: rhs.keySet()) {
            Map<? extends K2, ? extends E> data = rhs.get(key1);

            if (data == null)
                continue;

            m_map.put(key1, new HashMap<K2, E>(data));
        }
    }

    public Map<K1, Map<K2, E>> getFullMap() {
        return m_map;
    }

    protected Map<K2, E> getOrCreate(K1 key) {
        if (!m_map.containsKey(key)) {
            m_map.put(key, new HashMap<K2, E>());
        }
        return m_map.get(key);
    }

    public void put(K1 key, Map<? extends K2, ? extends E> partial) {
        Map<K2, E> _partial = getOrCreate(key);
        _partial.putAll(partial);
    }

    public void remove(K1 key) {
        m_map.remove(key);
    }

    public void put(K1 key1, K2 key2, E data) {
        Map<K2, E> _partial = getOrCreate(key1);
        _partial.put(key2, data);
    }

    public void remove(K1 key1, K2 key2) {
        if (!m_map.containsKey(key1))
            return;
        Map<K2, E> partial = m_map.get(key1);
        partial.remove(key2);
    }

    public Map<K2, E> get(K1 key) {
        return m_map.get(key);
    }

    public E get(K1 key1, K2 key2) {
        Map<K2, E> partial = get(key1);
        if (partial == null)
            return null;

        return partial.get(key2);
    }
}
