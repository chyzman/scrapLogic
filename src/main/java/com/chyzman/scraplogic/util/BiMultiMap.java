package com.chyzman.scraplogic.util;

import io.wispforest.owo.serialization.Endec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BiMultiMap<K, V> {
    private final Map<K, Set<V>> values = new HashMap<>();

    private final Map<V, Set<K>> keys = new HashMap<>();

    //region ENDEC STUFF

    public static <K, V> Endec<BiMultiMap<K, V>> endec(Endec<K> keyEndec, Endec<V> valueEndec) {
        return Endec.map(keyEndec, valueEndec.setOf()).xmap(valueMap -> {
            var map = new HashMap<V, Set<K>>();
            valueMap.forEach((key, values) -> values.forEach(value -> map.computeIfAbsent(value, v -> new HashSet<>()).add(key)));
            return map;
        }, map -> map.values);
    }

    //endregion

    public Set<K> keys(V value) {
        return keys.get(value);
    }

    public Set<V> values(K key) {
        return values.get(key);
    }

    public BiMultiMap<K, V> put(K key, V value) {
        values.computeIfAbsent(key, k -> new HashSet<>()).add(value);
        keys.computeIfAbsent(value, v -> new HashSet<>()).add(key);
        return this;
    }

    public BiMultiMap<K, V> remove(K key, V value) {
        values.get(key).remove(value);
        keys.get(value).remove(key);
        return this;
    }
}
