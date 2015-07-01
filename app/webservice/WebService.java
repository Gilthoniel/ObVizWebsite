package webservice;

import com.google.gson.reflect.TypeToken;
import constants.Constants;
import models.PlayApp;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import play.Logger;
import play.libs.F;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    public F.Promise<PlayApp> getAppInfo(String id) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_APP));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("weight", Constants.WEIGHT.LIGHT.toString()));

        return ConnectionService.get(Constants.baseURL, params, PlayApp.class);
    }
}
