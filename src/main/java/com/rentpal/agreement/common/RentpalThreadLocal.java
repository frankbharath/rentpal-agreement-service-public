package com.rentpal.agreement.common;

/**
 * @author frank
 * @created 14 Dec,2020 - 9:05 PM
 */

import java.util.HashMap;

/**
 * The Class RentPalThreadLocal.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 24, 2020 12:01:13 AM
 * This class creates thread local for a given request that stores user id and user email.
 */

public class RentpalThreadLocal {

    /** The Constant LOCAL. */
    private static final ThreadLocal<HashMap<String, Object>> LOCAL = new ThreadLocal<>();

    /**
     * Inits the ThreadLocal with a HashMap.
     */
    public static void init() {
        LOCAL.set(new HashMap<String, Object>());
    }

    /**
     * Adds the values for given key to ThreadLocal.
     *
     * @param key the key
     * @param value the value
     */
    public static void add(String key, Object value) {
        LOCAL.get().put(key, value);
    }

    /**
     * Gets the values for given key.
     *
     * @param key the key
     * @return the object
     */
    public static Object get(String key) {
        HashMap<String, Object> map=LOCAL.get();
        return map.getOrDefault(key, null);
    }

    /**
     * Clears the ThreadLocal before a response is sent to the client.
     */
    public static void clear() {
        LOCAL.get().clear();
    }
}
