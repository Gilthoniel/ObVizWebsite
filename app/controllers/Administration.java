package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import models.AndroidApp;
import models.WebPage;
import models.WebPage.WebPath;
import models.admin.Argument;
import models.admin.Log;
import play.Logger;
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
import webservice.MessageParser;

import javax.inject.Inject;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gaylor on 15.07.15.
 * Administration panel for settings, etc ...
 */
@Security.Authenticated(Secured.class)
public class Administration extends Controller {

    @Inject
    private AdminWebService wb;
    @Inject
    private play.Application application;
    private List<WebPath> paths;

    public Administration() {
        paths = new LinkedList<>();
        paths.add(new WebPath(routes.Administration.admin(), "Crawler logs"));
        paths.add(new WebPath(routes.Administration.users(), "Users' rights"));
        paths.add(new WebPath(routes.Administration.training(), "Training"));
        paths.add(new WebPath(routes.Administration.logs(), "Logs"));
        paths.add(new WebPath(routes.Application.index(), "Back to website"));
    }

    /**
     * Logs of the crawlers
     * @return Html
     */
    public F.Promise<Result> admin() {
        WebPage webpage = new WebPage(session(), paths);
        if (webpage.getUser() == null || webpage.getUser().right != BaseUserService.Rights.ADMIN) {
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

    /**
     * Change users rights
     * @return Html
     */
    public Result users() {
        WebPage webpage = new WebPage(session(), paths);
        if (webpage.getUser() == null || webpage.getUser().right != BaseUserService.Rights.ADMIN) {
            return redirect(routes.Application.index());
        }

        List<BaseUser> users = BaseUser.find.all();

        webpage.getBreadcrumb().get(1).activate();
        return ok((play.twirl.api.Html) views.html.administration.users.render(webpage, users));
    }

    /**
     * Page to entrain the AI model
     * @return Html
     */
    public Result training() {
        WebPage webpage = new WebPage(session(), paths);
        if (webpage.getUser() == null || webpage.getUser().right != BaseUserService.Rights.ADMIN) {
            return redirect(routes.Application.index());
        }

        return ok((play.twirl.api.Html) views.html.administration.training.render(webpage));
    }

    public Result logs() {
        WebPage webpage = new WebPage(session(), paths);
        if (webpage.getUser() == null || webpage.getUser().right != BaseUserService.Rights.ADMIN) {
            return redirect(routes.Application.index());
        }

        File logsDirectory = application.getFile("logs/archived");

        File[] files = logsDirectory.listFiles();

        return ok((play.twirl.api.Html) views.html.administration.app_logs.render(webpage, Arrays.asList(files)));
    }

    /** AJAX **/

    public F.Promise<Result> loadLogs() {

        final BaseUser user = Login.getLocalUser(session());
        if (user == null || user.right != BaseUserService.Rights.ADMIN) {
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
        if (user == null || user.right != BaseUserService.Rights.ADMIN) {
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

    public F.Promise<Result> proposeArgument() {

        DynamicForm form = Form.form().bindFromRequest();
        String json = form.get("json");

        return wb.proposeArgument(json).map(result -> {

            if (result != null && result) {
                return ok();
            } else {
                return badRequest();
            }
        });
    }

    public F.Promise<Result> getParsedApplications() {

        int pageNumber = MessageParser.parseInt(request().getQueryString("p"));
        int numberPerPage = Constants.NUMBER_PARSED_APP_PER_PAGE;

        F.Promise<List<AndroidApp>> promiseApps = wb.getParsedApps(pageNumber, numberPerPage);
        F.Promise<List<Argument>> promiseArguments = wb.getArguments();

        return promiseArguments.flatMap(tArguments -> {

            final Set<String> appIDs = new HashSet<>();
            if (tArguments != null) {
                appIDs.addAll(tArguments.stream().map(Argument::getAppID).collect(Collectors.toList()));
            }

            return promiseApps.map(androidApps -> {

                if (androidApps == null) {
                    androidApps = new ArrayList<>();
                }

                ArrayNode root = Json.newArray();

                for (AndroidApp app : androidApps) {
                    root.add(views.html.templates.admin_play_app.render(app, appIDs).toString());
                }

                return ok(root);
            });
        });
    }

    public Result readFile() {

        String name = request().getQueryString("n");
        if (name == null) {
            return badRequest();
        }

        ArrayNode root = Json.newArray();

        try {

            File file = application.getFile("logs/" + name);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                root.add(line);
            }

        } catch (IOException e) {

            Logger.error(e.getMessage());
            return badRequest();
        }

        return ok(root);
    }
}
