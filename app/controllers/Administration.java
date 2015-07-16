package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import models.WebPage;
import models.WebPage.WebPath;
import models.admin.Log;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.BaseUser;
import service.BaseUserService;
import webservice.AdminWebService;

import java.util.*;

/**
 * Created by gaylor on 15.07.15.
 * Administration panel for settings, etc ...
 */
@Security.Authenticated(Secured.class)
public class Administration extends Controller {

    private AdminWebService wb;
    private List<WebPath> paths;

    public Administration() {
        wb = AdminWebService.getInstance();
        paths = new LinkedList<>();
        paths.add(new WebPath(routes.Administration.admin(), "Crawler logs"));
        paths.add(new WebPath(routes.Administration.users(), "Users' rights"));
        paths.add(new WebPath(routes.Application.index(), "Back to website"));
    }

    public F.Promise<Result> admin() {
        WebPage webpage = new WebPage(session(), paths);
        if (webpage.getUser().right != BaseUserService.Rights.ADMIN) {
            return F.Promise.pure(redirect(routes.Application.index()));
        }

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

            webpage.getBreadcrumb().get(0).activate();
            return ok((play.twirl.api.Html) views.html.administration.index.render(webpage, mappedLogs));
        });
    }

    public Result users() {
        WebPage webpage = new WebPage(session(), paths);
        if (webpage.getUser().right != BaseUserService.Rights.ADMIN) {
            return redirect(routes.Application.index());
        }

        List<BaseUser> users = BaseUser.find.all();

        webpage.getBreadcrumb().get(1).activate();
        return ok((play.twirl.api.Html) views.html.administration.users.render(webpage, users));
    }

    /** AJAX **/

    public F.Promise<Result> loadLogs() {

        final BaseUser user = Login.getLocalUser(session());
        if (user.right != BaseUserService.Rights.ADMIN) {
            return F.Promise.pure(badRequest("Not authorized"));
        }

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

    public Result setUserRight() {

        final BaseUser user = Login.getLocalUser(session());
        if (user.right != BaseUserService.Rights.ADMIN) {
            return badRequest("Not authorized");
        }

        DynamicForm form = Form.form().bindFromRequest();

        String email = form.get("email");
        BaseUserService.Rights right = BaseUserService.Rights.getByName(form.get("right"));

        BaseUser target = BaseUser.findByEmail(email);
        ObjectNode root = Json.newObject();

        if (target != null) {
            target.right = right;
            target.update();
            root.put("result", true);
        } else {
            root.put("result", false);
        }

        return ok(root);
    }
}
