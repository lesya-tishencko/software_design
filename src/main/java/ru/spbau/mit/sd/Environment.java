package ru.spbau.mit.sd;

import java.util.HashMap;
import java.util.Map;

/**
 * class wrapper to store environment
 */
public class Environment {
    private Map<String, String> map = new HashMap<>(11);

    /**
     * get value by key
     * @param key - key in map
     * @return - value associated with key
     */
    public String get(String key) {
        return map.get(key);
    }

    /**
     * put key into map if not exist, otherwise create new node with key-value pair
     * @param key - key
     * @param value - new value
     */
    public void put(String key, String value) {
        map.put(key, value);
    }

    /**
     * check if map contain this key
     * @param key - key
     * @return - true if contains, otherwise false
     */
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }
}
