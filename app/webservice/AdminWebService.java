package webservice;

import com.google.gson.reflect.TypeToken;
import constants.Constants;
import models.admin.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import play.libs.F;
import play.mvc.Result;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaylor on 15.07.15.
 * Webservice for the administration
 */
public class AdminWebService {

    private static AdminWebService instance = new AdminWebService();

    private AdminWebService() {};

    static public AdminWebService getInstance() {
        return instance;
    }

    public F.Promise<List<Log>> getLogs(String machine) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmd", Constants.GET_APPS_CRAWLER_LOGS));
        params.add(new BasicNameValuePair("machine", machine));
        params.add(new BasicNameValuePair("nb_logs", String.valueOf(50)));
        
        Type type = new TypeToken<List<Log>>() {}.getType();
        return ConnectionService.getNoCache(Constants.adminURL, params, type);
    }
}
