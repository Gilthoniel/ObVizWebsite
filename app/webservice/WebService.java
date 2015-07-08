package webservice;

import com.google.gson.reflect.TypeToken;
import constants.Constants;
import models.Initiatable;
import models.AndroidApp;
import models.TopicTitles;
import models.errors.NoAppFoundException;
import models.errors.ServerOverloadedException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import play.libs.F;

import java.lang.reflect.Type;
import java.util.*;

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

        Type type = new TypeToken<List<String>>() {}.getType();
        return ConnectionService.<List<String>>get(Constants.baseURL, params, type).recover(throwable -> {

            throw new ServerOverloadedException();
        });
    }

    /**
     * Get the information about one application
     * @param id of the application
     * @param weight LIGHT or FULL
     * @return the object with the information
     */
    public F.Promise<AndroidApp> getAppDetails(String id, Constants.Weight weight) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_APP));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("weight", weight.toString()));

        F.Promise<AndroidApp> promise = ConnectionService.get(Constants.baseURL, params, AndroidApp.class,
                new ServerOverloadedException(), new NoAppFoundException());

        if (weight == Constants.Weight.FULL) {
            return initPromise(promise);
        } else {
            return promise;
        }
    }

    /**
     * Get the information about applications with IDs given
     * @param ids list of the IDs
     * @param weight of the request
     * @return list of AndroidApp
     */
    public F.Promise<List<AndroidApp>> getAppDetails(List<String> ids, Constants.Weight weight) {

        List<F.Promise<AndroidApp>> promises = new LinkedList<>();
        for (String id : ids) {
            promises.add(getAppDetails(id, weight).recover(throwable -> null));
        }

        return F.Promise.sequence(promises).map(result -> {
            List<AndroidApp> apps = new ArrayList<>(result);
            Iterator<AndroidApp> it = apps.iterator();
            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                }
            }

            return apps;
        });
    }

    /**
     * Get the name of the topics for an id
     * @return Map : key is the id and value is the array of titles
     */
    public F.Promise<Map<Integer, List<String>>> getTopicTitles() {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_TOPIC_TITLES));

        Type type = new TypeToken<List<TopicTitles>>() {}.getType();
        // Return the list of topics in mapped form for easiest uses
        return ConnectionService.<List<TopicTitles>>get(Constants.baseURL, params, type).map(titles -> {
            Map<Integer, List<String>> mappedTitles = new TreeMap<>();
            for (TopicTitles title : titles) {
                List<String> upper = new ArrayList<>();
                for (String opinion : title.getTitles()) {
                    upper.add(opinion.substring(0, 1).toUpperCase() + opinion.substring(1));
                }

                mappedTitles.put(title.getID(), upper);
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
