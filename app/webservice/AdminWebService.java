package webservice;

import com.google.gson.reflect.TypeToken;
import constants.Constants;
import models.AndroidApp;
import models.Review;
import models.admin.Argument;
import models.admin.Log;
import models.errors.NoAppFoundException;
import models.errors.ServerOverloadedException;
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
import java.util.List;

/**
 * Created by gaylor on 15.07.15.
 * Webservice for the administration
 */
@Singleton
public class AdminWebService {

    @Inject
    private ConnectionService service;

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

        /*
        return service.post(Constants.adminURL, encodeValues(params));
        //*/

        //*
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

    public F.Promise<Review.ReviewContainer> getReviews(String id, int pageNumber) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_REVIEWS));
        params.add(new BasicNameValuePair("id", id));
        if (pageNumber >= 0) {
            params.add(new BasicNameValuePair("page_nr", String.valueOf(pageNumber)));
        }
        params.add(new BasicNameValuePair("nb_per_page", "4000"));

        return service.get(Constants.baseURL, params, Review.ReviewContainer.class, null,
                new ServerOverloadedException(), new NoAppFoundException());
    }

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
