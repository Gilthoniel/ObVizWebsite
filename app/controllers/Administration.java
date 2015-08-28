package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import models.*;
import models.WebPage.WebPath;
import models.admin.Argument;
import models.admin.Log;
import models.errors.AJAXRequestException;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;
import service.BaseUser;
import service.BaseUserService;
import service.CategoryManager;
import service.TopicsManager;
import service.cache.CustomCache;
import webservice.AdminWebService;
import webservice.MessageParser;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gaylor on 15.07.15.
 * Administration panel for settings, etc ...
 */
@Security.Authenticated(Secured.class)
@With(WebPageAdmin.class)
public class Administration extends Controller {

    @Inject
    private AdminWebService wb;
    @Inject
    private play.Application application;
    @Inject
    private CustomCache cache;

    private List<WebPath> paths;

    public Administration() {
        paths = new LinkedList<>();
        paths.add(new WebPath(routes.Administration.logs(), "Logs"));
        paths.add(new WebPath(routes.Administration.crawlers(), "Crawlers"));
        paths.add(new WebPath(routes.Administration.training(), "Training"));
        paths.add(new WebPath(routes.Administration.topics(), "Topics"));
        paths.add(new WebPath(routes.Administration.categories(), "Categories"));
        paths.add(new WebPath(routes.Administration.categoriesTypes(), "Types"));
        paths.add(new WebPath(routes.Administration.users(), "Users"));
        paths.add(new WebPath(routes.Application.index(), "Back to website"));
    }

    /**
     * Logs of the crawlers
     * @return Html
     */
    public F.Promise<Result> crawlers() {
        WebPage webpage = getWebpage();

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

            webpage.getBreadcrumb().get(1).activate();
            return ok((play.twirl.api.Html) views.html.administration.index.render(webpage, mappedLogs));
        });
    }

    /**
     * Change users rights
     * @return Html
     */
    public Result users() {
        WebPage webpage = getWebpage();

        List<BaseUser> users = BaseUser.find.all();

        webpage.getBreadcrumb().get(6).activate();
        return ok((play.twirl.api.Html) views.html.administration.users.render(webpage, users));
    }

    /**
     * Page to entrain the AI model
     * @return Html
     */
    public Result training() {
        WebPage webpage = getWebpage();

        webpage.getBreadcrumb().get(2).activate();
        return ok((play.twirl.api.Html) views.html.administration.training.render(webpage));
    }

    /**
     * Get the server logs
     * @return
     */
    public Result logs() {
        WebPage webpage = getWebpage();

        File logsDirectory = application.getFile("logs/archived");

        File[] files = logsDirectory.listFiles();
        List<File> list = null;
        if (files != null) {
            list = Arrays.asList(files);
        }

        Collections.sort(list);
        Collections.reverse(list);

        webpage.getBreadcrumb().get(0).activate();
        return ok((play.twirl.api.Html) views.html.administration.app_logs.render(webpage, list));
    }

    public F.Promise<Result> topics() {
        WebPage webpage = getWebpage();

        return wb.getTopics().flatMap(topics -> wb.getCategories().map(categories -> {

            webpage.getBreadcrumb().get(3).activate();
            return ok((play.twirl.api.Html) views.html.administration.topics.render(webpage, topics, categories));
        }));

    }

    public F.Promise<Result> updateTopic() {

        DynamicForm form = Form.form().bindFromRequest(request());
        TopicTitles topic = new TopicTitles(form);

        if (topic.isValid()) {

            //*
            return wb.updateTopic(MessageParser.toJson(topic)).map(result -> {

                if (result != null) {
                    successMessage();
                    // Clear the cache to force an update of the data
                    cache.getPinnedCache().remove(TopicsManager.CACHE_KEY);
                } else {
                    errorMessage();
                }

                return redirect(routes.Administration.topics());
            });
            //*/
        } else {

            Logger.info("Bad json: " + MessageParser.toJson(topic));
            errorMessage();
        }

        return F.Promise.pure(redirect(routes.Administration.topics()));
    }

    public F.Promise<Result> categories() {
        WebPage webpage = getWebpage();

        return wb.getCategories().flatMap(categories -> wb.getCategoryTypes().map(types -> {

            webpage.getBreadcrumb().get(4).activate();
            return ok((play.twirl.api.Html) views.html.administration.categories.render(webpage, categories, types));
        }));
    }

    public F.Promise<Result> updateCategory() {
        DynamicForm form = Form.form().bindFromRequest();
        Category category = new Category(form);

        if (category.isValid()) {

            return wb.updateCategory(MessageParser.toJson(category)).map(result -> {

                if (result != null) {
                    successMessage();
                    // Clean the cache to force the update
                    cache.getPinnedCache().remove(CategoryManager.CACHE_KEY);
                } else {
                    errorMessage();
                }

                return redirect(routes.Administration.categories());
            });
        } else {

            errorMessage();
            Logger.info("Bad json: " + MessageParser.toJson(category));
        }

        return F.Promise.pure(redirect(routes.Administration.categories()));
    }

    public F.Promise<Result> categoriesTypes() {
        WebPage webpage = getWebpage();

        return wb.getCategoryTypes().map(types -> {

            webpage.getBreadcrumb().get(5).activate();
            return ok((play.twirl.api.Html) views.html.administration.category_types.render(webpage, types));
        });
    }

    public F.Promise<Result> insertType() {
        DynamicForm form = Form.form().bindFromRequest();
        CategoryType type = new CategoryType(form);

        if (type.isValid()) {
            return wb.insertCategoryType(MessageParser.toJson(type)).map(result -> {

                if (result != null) {
                    successMessage();
                    // Clean the cache to force the update
                    cache.getPinnedCache().remove(CategoryManager.CACHE_KEY);
                } else {
                    errorMessage();
                }

                return redirect(routes.Administration.categoriesTypes());
            });
        } else {

            errorMessage();
            Logger.info("Bad json: "+MessageParser.toJson(type));
        }

        return F.Promise.pure(redirect(routes.Administration.categoriesTypes()));
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

    public Result setUserRight() {

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

        F.Promise<AndroidApp.Pager> promiseApps = wb.getParsedApps(pageNumber, numberPerPage);
        F.Promise<List<Argument>> promiseArguments = wb.getArguments();

        return promiseArguments.flatMap(tArguments -> {

            final Set<String> appIDs = new HashSet<>();
            if (tArguments != null) {
                appIDs.addAll(tArguments.stream().map(Argument::getAppID).collect(Collectors.toList()));
            }

            return promiseApps.map(pager -> {

                ObjectNode root = Json.newObject();

                ArrayNode apps = root.putArray("applications");
                for (AndroidApp app : pager.apps) {
                    apps.add(views.html.templates.admin_play_app.render(app, appIDs).toString());
                }

                root.put("nbPage", pager.nbTotalPages);
                return ok(root);
            });
        });
    }

    public F.Promise<Result> getAdminReviews() throws AJAXRequestException {

        final String appID = request().getQueryString("id");
        final int page = MessageParser.parseInt(request().getQueryString("p"));
        if (page < 0) {
            throw new AJAXRequestException();
        }

        F.Promise<Review.ReviewContainer> promise = wb.getReviews(appID, page);
        return promise.map(container -> {

            ArrayNode root = Json.newArray();

            Collections.sort(container.reviews, (review, other) -> {

                int nbReview = review.opinions != null ? review.opinions.nbOpinions : 0;
                int nbOther = other.opinions != null ? other.opinions.nbOpinions : 0;

                return Integer.compare(nbReview, nbOther);
            });
            Collections.reverse(container.reviews);

            for (Review review : container.reviews) {
                if (review.parsed && review.parsedBody.size() > 0 && review.reviewBody.length() > 15) {
                    root.add(views.html.templates.admin_review.render(review).toString());
                }
            }

            return ok(root);
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

    public Result resetCache() {
        final BaseUser user = Login.getLocalUser(session());

        cache.clear();
        Logger.info("Cache reset by " + user.name);

        return ok();
    }

    private void errorMessage() {
        flash("error", "Json is not valid for modification.");
    }

    private void successMessage() {
        flash("success", "Successfully update topic");
    }

    private WebPage getWebpage() {

        WebPage webpage = (WebPage) Http.Context.current().args.get("com.obviz.webpage");
        webpage.addAllPaths(paths);

        return webpage;
    }
}
