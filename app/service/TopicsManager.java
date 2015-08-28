package service;

import com.google.inject.Inject;
import constants.Constants;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import play.Logger;
import service.cache.CustomCache;
import webservice.WebService;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaylor on 08/19/2015.
 *
 */
@Singleton
public class TopicsManager {

    public static final String CACHE_KEY = "com.obviz.topics";

    private WebService mWebservice;
    private Cache mCache;

    @Inject
    public TopicsManager(WebService webservice, CustomCache cache) {
        mWebservice = webservice;
        mCache = cache.getPinnedCache();
    }

    public String getTitle(int topicID, boolean firstTry) {

        Container container = getContainer();

        if (container.titles.containsKey(topicID)) {

            return container.titles.get(topicID);
        } else if (firstTry) {

            mCache.remove(CACHE_KEY);
            return getTitle(topicID, false);
        } else {

            return "Unknown";
        }
    }

    public String getTitle(int topicID) {

        return getTitle(topicID, true);
    }

    private synchronized Container init() {

        if (mCache.isElementInMemory(CACHE_KEY)) {
            return (Container) mCache.get(CACHE_KEY).getObjectValue();
        }

        Container container = new Container();
        return mWebservice.getTopicTitles().map(topics -> {

            container.titles = new HashMap<>();
            container.titles.putAll(topics);

            mCache.put(new Element(CACHE_KEY, container));
            return container;
        }).get(Constants.TIMEOUT);
    }

    private Container getContainer() {
        Element element = mCache.get(CACHE_KEY);
        if (element != null) {
            return (Container) element.getObjectValue();
        } else {

            return init();
        }
    }

    private class Container {

        private Map<Integer, String> titles;
    }
}
