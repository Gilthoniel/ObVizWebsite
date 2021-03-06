package webservice;

import com.google.gson.reflect.TypeToken;
import constants.Constants;
import models.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import play.Logger;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gaylor on 25.06.15.
 * WebService to get data from the Back-End
 */
@Singleton
public class WebService {

    @Inject
    private ConnectionService service;
    @Inject
    private MessageParser parser;

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

        String cacheKey = "details:" + id + ":" + weight;
        return service.get(Constants.baseURL, params, AndroidApp.class, cacheKey);
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
            promises.add(getAppDetails(id, weight));
        }

        return F.Promise.sequence(promises).map(result -> {
            List<AndroidApp> apps = new ArrayList<>(result);
            Iterator<AndroidApp> it = apps.iterator();
            while (it.hasNext()) {
                AndroidApp app = it.next();

                if (app == null || !app.isParsed()) {
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
    public F.Promise<Review.ReviewContainer> getReviews(String id, int topicID, int pageNumber, int size, String order) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_REVIEWS));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("topic_id", String.valueOf(topicID)));
        params.add(new BasicNameValuePair("order", order));
        if (pageNumber >= 0) {
            params.add(new BasicNameValuePair("page_nr", String.valueOf(pageNumber)));
        }

        if (size > 0) {
            params.add(new BasicNameValuePair("nb_per_page", String.valueOf(size)));
        }

        String cacheKey = "reviews:" + id + ":" + topicID + ":" + pageNumber + ":" + size + ":" + order;
        return service.get(Constants.baseURL, params, Review.ReviewContainer.class, cacheKey);
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
            params.add(new BasicNameValuePair("categories", parser.toJson(categories)));
        }

        String cacheKey = "search:" + name + ":" + String.join(":", categories);
        return service.get(Constants.baseURL, params,
                new TypeToken<List<AndroidApp>>() {}.getType(), cacheKey);
    }

    public F.Promise<List<DiscoverItem>> discover(String name, List<String> categories) {

        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair("cmd", Constants.DISCOVER_APPS));
        params.add(new BasicNameValuePair("name", name));
        if (categories.size() > 0) {
            params.add(new BasicNameValuePair("categories", parser.toJson(categories)));
        }

        return service.getNoCache(Constants.baseURL, params, new TypeToken<List<DiscoverItem>>() {
        }.getType());
    }

    /**
     * Get the list of trending applications for a category or in general if null or empty
     * @param categories Array of categories. Can be null or empty
     * @return the list of AndroidApp
     */
    public F.Promise<List<AndroidApp>> getTrending(List<String> categories) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_TRENDING_APPS));
        if (categories.size() > 0) {
            params.add(new BasicNameValuePair("categories", parser.toJson(categories)));
        }

        String cacheKey = "trending:" + String.join(":", categories);
        return service.get(Constants.baseURL, params, new TypeToken<List<AndroidApp>>() {
        }.getType(), cacheKey);
    }

    /**
     * Get the headline for the home page
     * @return a head line
     */
    public F.Promise<HeadLine> getHeadLine() {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_HEADLINE));

        return service.getNoCache(Constants.baseURL, params, HeadLine.class);
    }

    /**
     * Get the name of the topics for an id
     * @return Map : key is the id and value is the array of titles
     */
    public F.Promise<List<Topic>> getTopicTitles() {

        Logger.info("__TOPICS__ : Connection open to load");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_TOPIC_TITLES));

        Type type = new TypeToken<List<Topic>>() {}.getType();
        return service.<List<Topic>>getNoCache(Constants.baseURL, params, type);
    }

    /**
     * Get the categories
     * @return list of categories
     */
    public F.Promise<List<Category>> getCategories() {

        Logger.info("__CATEGORIES__ : Connection open to load");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_CATEGORIES));

        Type type = new TypeToken<List<Category>>(){}.getType();
        return service.getNoCache(Constants.baseURL, params, type);
    }

    public F.Promise<List<CategoryType>> getCategoryTypes() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_CATEGORIES_TYPES));

        Type type = new TypeToken<List<CategoryType>>(){}.getType();
        return service.getNoCache(Constants.baseURL, params, type);
    }

    /* POST request */

    public F.Promise<Boolean> markViewed(String appID) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.APP_VIEWED));
        params.add(new BasicNameValuePair("id", appID));

        return service.post(Constants.baseURL, params);
    }
}
