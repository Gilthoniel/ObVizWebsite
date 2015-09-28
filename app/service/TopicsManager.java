package service;

import com.google.inject.Inject;
import constants.Constants;
import models.Topic;
import play.Logger;
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
    private Container mContainer;
    private final Object mLock = new Object();

    @Inject
    public TopicsManager(WebService webservice) {
        mWebservice = webservice;

        init();
    }

    public String getTitle(int topicID) {

        Topic topic = getTopic(topicID);
        if (topic != null) {

            return topic.getTitle();
        } else {

            return "Unknown";
        }
    }

    public Topic getTopic(int topicID) {

        return getTopic(topicID, true);
    }

    private Topic getTopic(int topicID, boolean firstTry) {

        synchronized (mLock) {
            while (mContainer == null) {
                try {
                    mLock.wait();
                } catch (InterruptedException ignored) {}
            }

            if (mContainer.titles.containsKey(topicID)) {

                return mContainer.titles.get(topicID);
            } else if (firstTry) {

                init();
                return getTopic(topicID, false);
            } else {

                Logger.info("Can't find the topic with the id " + topicID);
                return null;
            }
        }
    }

    public void init() {

        synchronized (mLock) {
            mContainer = new Container();
            mContainer.titles = new HashMap<>();
            mWebservice.getTopicTitles().map(topics -> {

                for (Topic topic : topics) {
                    mContainer.titles.put(topic.getID(), topic);
                }

                return null;
            }).get(Constants.TIMEOUT);

            mLock.notifyAll();
        }
    }

    private class Container {

        private Map<Integer, Topic> titles;
    }
}
