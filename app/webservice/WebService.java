package webservice;

import com.google.gson.reflect.TypeToken;
import constants.Constants;
import models.Initiatable;
import models.PlayApp;
import models.TopicTitles;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import play.libs.F;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gaylor on 25.06.15.
 * WebService to get data from the Back-End
 */
public class WebService {

    private static WebService instance = new WebService();

    private WebService() {}

    /**
     * Get the Singleton instance
     * @return the instance
     */
    public static WebService getInstance() {
        return instance;
    }

    /**
     * Return all the IDs of the Play Applications
     * @return a list
     */
    public F.Promise<List<String>> getAppIDs() {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_APP_IDS));

        Type type = new TypeToken<List<String>>(){}.getType();
        return ConnectionService.get(Constants.baseURL, params, type);
    }

    /**
     * Get the information about one application
     * @param id of the application
     * @param weight LIGHT or FULL
     * @return the object with the information
     */
    public F.Promise<PlayApp> getAppInfo(String id, Constants.Weight weight) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_APP));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("weight", weight.toString()));

        if (weight == Constants.Weight.FULL) {
            return initPromise(ConnectionService.get(Constants.baseURL, params, PlayApp.class));
        } else {
            return ConnectionService.get(Constants.baseURL, params, PlayApp.class);
        }
    }

    public F.Promise<Map<Integer, List<String>>> getTopicTitles() {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_TOPIC_TITLES));

        Type type = new TypeToken<List<TopicTitles>>(){}.getType();
        // Return the list of topics in mapped form for easiest uses
        return ConnectionService.<List<TopicTitles>>get(Constants.baseURL, params, type).map(titles -> {
            Map<Integer, List<String>> mappedTitles = new TreeMap<>();
            for (TopicTitles title : titles) {

                mappedTitles.put(title.getID(), title.getTitles());
            }

            return mappedTitles;
        });
    }

    /* PRIVATE */

    /**
     * Init the object gotten by the request
     * @param promise which returns the object
     * @param <T> type of the object
     * @return a new promise with the object initiated
     */
    private <T extends Initiatable> F.Promise<T> initPromise(F.Promise<T> promise) {

        return promise.map(t -> {
            if (t != null) {
                t.init();
            }
            return t;
        });
    }
}
