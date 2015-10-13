package webservice;

import com.google.gson.reflect.TypeToken;
import constants.Constants;
import models.*;
import models.admin.Argument;
import models.admin.Log;
import models.admin.Report;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import play.Logger;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gaylor on 15.07.15.
 * Webservice for the administration
 */
@Singleton
public class AdminWebService {

    @Inject
    private ConnectionService service;

    public F.Promise<List<Report>> getReports() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_REPORTED_BUGS));

        Type type = new TypeToken<List<Report>>(){}.getType();
        return service.getNoCache(Constants.adminURL, params, type);
    }

    /**
     * Get the list of parsed applications
     * @return list of application
     */
    public F.Promise<AndroidApp.Pager> getParsedApps(int pageNumber, int numberPerPage) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_PARSED_APP));
        params.add(new BasicNameValuePair("page_nr", String.valueOf(pageNumber)));
        params.add(new BasicNameValuePair("nb_per_page", String.valueOf(numberPerPage)));

        return service.getNoCache(Constants.adminURL, params, AndroidApp.Pager.class);
    }

    /**
     * Get the logs of the crawler
     * @param machine name of the machine
     * @return the list of logs
     */
    public F.Promise<List<Log>> getLogs(String machine) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_APPS_CRAWLER_LOGS));
        params.add(new BasicNameValuePair("machine", machine));
        params.add(new BasicNameValuePair("nb_logs", String.valueOf(50)));
        
        Type type = new TypeToken<List<Log>>() {}.getType();
        return service.getNoCache(Constants.adminURL, params, type);
    }

    /**
     * Propose an argument for a review
     * @param json JSON with data of the argument
     * @return true if success
     */
    public F.Promise<Boolean> proposeArgument(String json) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.PROPOSE_ARGUMENT));
        params.add(new BasicNameValuePair("argument", json));

        //*
        return service.post(Constants.adminURL, encodeValues(params));
        //*/

        /*
        Logger.info(json);
        return F.Promise.pure(false);
        //*/
    }

    /**
     * Get the list of proposed arguments
     * @return list of arguments
     */
    public F.Promise<List<Argument>> getArguments() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_ARGUMENTS));

        Type type = new TypeToken<List<Argument>>(){}.getType();
        return service.getNoCache(Constants.adminURL, params, type);
    }

    /**
     * Get a list of reviews
     * @param id Application ID
     * @param pageNumber Page Number
     * @return list of reviews
     */
    public F.Promise<Review.ReviewContainer> getReviews(String id, int pageNumber) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_REVIEWS));
        params.add(new BasicNameValuePair("id", id));
        if (pageNumber >= 0) {
            params.add(new BasicNameValuePair("page_nr", String.valueOf(pageNumber)));
        }
        params.add(new BasicNameValuePair("nb_per_page", "4000"));

        return service.get(Constants.baseURL, params, Review.ReviewContainer.class, null);
    }

    /**
     * Get the list of topics
     * @return list of topics
     */
    public F.Promise<List<Topic>> getTopics() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_APP_TOPICS));

        return service.getNoCache(Constants.adminURL, params, new TypeToken<List<Topic>>() {
        }.getType());
    }

    /**
     * Update a topic in the database
     * @param json Object Topic
     * @return true if success, else false
     */
    public F.Promise<Topic> updateTopic(String json) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.UPDATE_TOPIC));
        params.add(new BasicNameValuePair("topic", json));

        //*
        return service.post(Constants.adminURL, encodeValues(params), Topic.class);
        //*/

        /*
        Logger.info(json);
        return F.Promise.pure(null);
        //*/
    }

    public F.Promise<List<Category>> getCategories() {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", "Get_App_Categories"));

        Type type = new TypeToken<List<Category>>(){}.getType();
        return service.getNoCache(Constants.adminURL, params, type);
    }

    public F.Promise<List<CategoryType>> getCategoryTypes() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", "Get_App_Categories_Types"));

        Type type = new TypeToken<List<CategoryType>>(){}.getType();
        return service.getNoCache(Constants.adminURL, params, type);
    }

    public F.Promise<CategoryType> insertCategoryType(String json) {
        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair("cmd", Constants.INSERT_TYPE));
        params.add(new BasicNameValuePair("category_type", json));

        //*
        return service.post(Constants.adminURL, encodeValues(params), CategoryType.class);
        //*/

        /*
        Logger.info(json);
        return F.Promise.pure(true);
        //*/
    }

    public F.Promise<Category> updateCategory(String json) {
        List<NameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair("cmd", Constants.UPDATE_CATEGORY));
        params.add(new BasicNameValuePair("category", json));

        //*
        return service.post(Constants.adminURL, params, Category.class);
        //*/

        /*
        Logger.info(json);
        return F.Promise.pure(null);
        //*/
    }

    /* PRIVATE FUNCTIONS */

    private List<NameValuePair> encodeValues(List<NameValuePair> params) {
        List<NameValuePair> values = new ArrayList<>();

        for (NameValuePair value : params) {
            try {
                values.add(new BasicNameValuePair(value.getName(), URLEncoder.encode(value.getValue(), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                Logger.error("When trying to encore url : " + e.getMessage());
            }
        }

        return values;
    }
}
