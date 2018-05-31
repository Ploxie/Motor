package org.ploxie.utils;

import java.util.HashMap;
import java.util.Map;

public class CacheMap<K, V> extends HashMap<K, V> {

    private int capacity;
    private Map<K, Long> keysLastUsedTimesMap = new HashMap<>();

    public CacheMap(final int capacity) {
        this.capacity = capacity;
    }

    public void use(final K key) {
        keysLastUsedTimesMap.put(key, System.currentTimeMillis());
    }

    @Override
    public V put(K key, V value) {
        if (size() > capacity) {
            K lowest = null;
            long lowestLastTime = -1;
            for (Entry<K, Long> entry : keysLastUsedTimesMap.entrySet()) {
                long lastUsedTime = entry.getValue();
                if (lastUsedTime < lowestLastTime) {
                    lowest = entry.getKey();
                    lowestLastTime = lastUsedTime;
                }
            }
            if (lowest != null) {
                keysLastUsedTimesMap.remove(lowest);
                remove(lowest);
            }
        }
        return super.put(key, value);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(final int capacity) {
        this.capacity = capacity;
    }

}
