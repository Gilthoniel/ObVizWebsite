package service;

import com.google.inject.Inject;
import constants.Constants;
import play.libs.F;
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

    @Inject
    private WebService webservice;

    private Map<Integer, String> titles = new HashMap<>();

    public String getTitle(int topicID) {

        if (titles.containsKey(topicID)) {

            return titles.get(topicID);
        } else {

            return webservice.getTopicTitles().map(topics -> {

                titles.putAll(topics);

                if (titles.containsKey(topicID)) {
                    return titles.get(topicID);
                } else {
                    return "";
                }
            }).recover(throwable -> "").get(Constants.TIMEOUT);
        }
    }
}
