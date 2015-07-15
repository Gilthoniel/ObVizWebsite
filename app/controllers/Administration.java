package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import models.admin.Log;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import webservice.AdminWebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaylor on 15.07.15.
 * Administration panel for settings, etc ...
 */
public class Administration extends Controller {

    private AdminWebService wb = AdminWebService.getInstance();

    public F.Promise<Result> admin() {

        List<F.Promise<List<Log>>> promises = new ArrayList<>();
        for (String machine : Constants.MACHINES) {
            promises.add(wb.getLogs(machine));
        }

        return F.Promise.sequence(promises).map(logs -> {
            Map<String, List<Log>> mappedLogs = new HashMap<>();
            for (List<Log> list : logs) {

                if (list != null && list.size() > 0) {
                    mappedLogs.put(list.get(0).getMachine(), list);
                }
            }

            return ok((play.twirl.api.Html) views.html.administration.index.render(mappedLogs));
        });
    }

    /** AJAX **/

    public F.Promise<Result> loadLogs() {

        List<F.Promise<List<Log>>> promises = new ArrayList<>();
        for (String machine : Constants.MACHINES) {
            promises.add(wb.getLogs(machine));
        }

        return F.Promise.sequence(promises).map(logs -> {
            ObjectNode root = Json.newObject();
            for (List<Log> list : logs) {

                if (list != null && list.size() > 0) {

                    root.put(list.get(0).getMachine(), views.html.administration.logs.render(list).toString());
                }
            }

            return ok(root);
        });
    }
}
