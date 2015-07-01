package webservice;

import constants.Constants;
import play.cache.Cache;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by gaylor on 25.06.15.
 * Cache layout to avoid useless web interactions
 */
public class CustomCache extends Cache {
    private static volatile Set<String> keys = new LinkedHashSet<>();

    /**
     * Return the object in the cache or null if it doesn't exist
     * @param key key of the object in the hash
     * @return the object or null
     */
    public static <T> T take(final String key) {

        return (T) get(key);
    }

    /**
     * Put or modify the object in the cache with the given key
     * @param key for the hash
     * @param object the object
     */
    public static synchronized void put(final String key, final Object object) {
        if (!key.isEmpty() && object != null) {
            keys.add(key);
            Cache.set(key, object, Constants.TIME_CACHE_EXPIRED);
        }
    }

    /**
     * Put or modify the object in the cache with the given key
     * @param key for the hash
     * @param object the object
     * @param expiration expiration time of the object in seconds
     */
    public static synchronized void put(final String key, final Object object, int expiration) {
        if (!key.isEmpty() && object != null) {
            keys.add(key);
            Cache.set(key, object, expiration);
        }
    }

    /**
     * Clean the content of the cache
     */
    public static synchronized void reset() {
        keys.forEach(Cache::remove);
        keys.clear();
    }

    /**
     * Return true if the key exists in the cache
     * @param key the key
     * @return true if the key exists
     */
    public static synchronized boolean contains(final String key) {
        return Cache.get(key) != null;
    }
}
