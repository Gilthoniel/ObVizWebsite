package webservice;

import com.google.gson.reflect.TypeToken;
import constants.Constants;
import models.Initiatable;
import models.AndroidApp;
import models.Review;
import models.TopicTitles;
import models.errors.NoAppFoundException;
import models.errors.ServerOverloadedException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import play.Logger;
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

        F.Promise<AndroidApp> promise = ConnectionService.get(Constants.baseURL, params, AndroidApp.class, true,
                new ServerOverloadedException(), new NoAppFoundException());

        return initPromise(promise);
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
            promises.add(initPromise(getAppDetails(id, weight).recover(throwable -> {
                Logger.error("Fail when attempt to get information for an app: "+id);

                return null;
            })));
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
     * Return the list of reviews for a specific application
     * @param id of the application
     * @param pageNumber number of the page
     * @param size number of reviews per page
     * @return list of reviews
     */
    public F.Promise<List<Review>> getReviews(String id, int pageNumber, int size) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_REVIEWS));
        params.add(new BasicNameValuePair("id", id));
        if (pageNumber >= 0) {
            params.add(new BasicNameValuePair("page_nr", String.valueOf(pageNumber)));
        }

        if (size > 0) {
            params.add(new BasicNameValuePair("nb_per_page", String.valueOf(size)));
        }

        Type type = new TypeToken<List<Review>>(){}.getType();
        return ConnectionService.get(Constants.baseURL, params, type, true,
                new ServerOverloadedException(), new NoAppFoundException());
    }

    public F.Promise<List<Review>> getReviews(String id) {

        return getReviews(id, -1, -1);
    }

    /**
     * Search an app with his name
     * @param name query of the search
     * @param categories list of categories where we have to search
     * @return the result
     */
    public F.Promise<List<AndroidApp>> search(String name, List<String> categories) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.SEARCH_APPS));
        params.add(new BasicNameValuePair("name", name));
        if (categories.size() > 0) {
            params.add(new BasicNameValuePair("categories", MessageParser.toJson(categories)));
        }

        Type type = new TypeToken<List<AndroidApp>>() {}.getType();
        return ConnectionService.get(Constants.baseURL, params, type);
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
