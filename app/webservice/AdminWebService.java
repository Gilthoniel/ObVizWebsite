package webservice;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import constants.Constants;
import models.admin.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import play.Logger;
import play.libs.F;
import play.mvc.Result;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaylor on 15.07.15.
 * Webservice for the administration
 */
public class AdminWebService {

    private static AdminWebService instance = new AdminWebService();

    private AdminWebService() {}

    static public AdminWebService getInstance() {
        return instance;
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
        return ConnectionService.getNoCache(Constants.adminURL, params, type);
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

        Logger.info("JSON: " + json);
        return ConnectionService.post(Constants.adminURL, encodeValues(params));
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
