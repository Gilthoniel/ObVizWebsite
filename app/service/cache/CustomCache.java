package service.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import play.Application;
import play.Logger;
import play.api.inject.ApplicationLifecycle;
import play.cache.CacheApi;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.util.concurrent.Callable;

/**
 * Created by gaylor on 25.06.15.
 * Cache layout to avoid useless web interactions
 */
@Singleton
public class CustomCache implements CacheApi {

    private CacheManager manager;
    private Cache cache;

    @Inject
    private CustomCache(ApplicationLifecycle lifecycle, Application application) {

        File file = application.getFile("conf/ehcache.xml");
        try {

            InputStream stream = new FileInputStream(file);
            manager = CacheManager.newInstance(stream);

            Logger.info("Cache configuration has been loaded");
            Logger.info("Cache file:" + file.getAbsolutePath());
            Logger.info("Cache on disk " + manager.getConfiguration().getDiskStoreConfiguration().getPath());

        } catch (FileNotFoundException e) {
            Logger.info("EHCache config file not found");
            Logger.info("Search for: " + file.getAbsolutePath());

            manager = CacheManager.getInstance();
        }

        cache = manager.getCache("cache-obviz");

        // Turn off the manager when Play stopped
        lifecycle.addStopHook(() -> {
            manager.shutdown();
            Logger.info("Cache manager has been well turn off");

            return F.Promise.pure(null);
        });
    }

    /**
     * Check in the memory cache and in the temp disk cache if the key exists and is in the cache
     * @param key Key that we want to check
     * @return True if found else False
     */
    public boolean contains(String key) {

        return (cache.isElementInMemory(key) || cache.isElementOnDisk(key)) && !cache.get(key).isExpired();
    }

    /**
     * Get an element of the cache and assume that the type is correct
     * @param key Key of the element
     * @param <T> Type of the element
     * @return The element or Null
     */
    @Override
    public <T> T get(String key) {

        Element element = cache.get(key);
        if (element != null && !element.isExpired()) {

            return (T) element.getObjectValue();

        } else {

            return null;

        }
    }

    /**
     * Get the result or get from the callable block
     * @param key Key of the element
     * @param callable Block which returns a default value
     * @param expiration Expiration time in millisecond
     * @param <T> Type of the element
     * @return The element or a default value or null
     */
    @Override
    public <T> T getOrElse(String key, Callable<T> callable, int expiration) {

        return F.Promise.pure(getOrElse(key, callable)).get(expiration);
    }

    /**
     * Get the result or return a default value
     * @param key Key of the result
     * @param callable Block which returns the default value
     * @param <T> Type of the values
     * @return The result or the default value or null if an exception occurred
     */
    @Override
    public <T> T getOrElse(String key, Callable<T> callable) {

        if (contains(key)) {

            return get(key);
        } else {
            try {

                return callable.call();

            } catch (Exception e) {

                Logger.error("GetOrElse callback throws exception");
                Logger.error(e.getMessage());

                return null;
            }
        }
    }

    /**
     * Put an element in the cache with an expiration time
     * @param key Key of the element
     * @param object Object to put in the cache. Must be Serializable
     * @param expiration Expiration time in millisecond
     */
    @Override
    public void set(String key, Object object, int expiration) {

        if (expiration > 0) {
            cache.put(new ExpirableElement(key, object, expiration));
        } else {
            cache.put(new Element(key, object));
        }
    }

    /**
     * Put an element in the cache without any expiration time
     * @param key Key of the object
     * @param object Value of the object
     */
    @Override
    public void set(String key, Object object) {

        set(key, object, 0);
    }

    /**
     * Remove the object in the cache
     * @param key Key of the object
     */
    @Override
    public void remove(String key) {

        cache.remove(key);
    }
}
