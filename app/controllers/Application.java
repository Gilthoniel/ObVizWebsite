package controllers;

import constants.Constants;
import constants.Constants.Category;
import models.AndroidApp;
import models.CategoryFilter;
import models.CategorySet;
import models.WebPage;
import models.errors.NoAppFoundException;
import models.errors.ServerOverloadedException;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import webservice.WebService;

import java.util.*;

public class Application extends Controller {

    private WebService wb;
    private CategoryFilter filters;

    public Application() {

        wb = WebService.getInstance();
        filters = new CategoryFilter();
    }

    /**
     * Homepage
     * @return html result
     */
    public Result index() {
        WebPage webpage = new WebPage(session());

        return ok((play.twirl.api.Html) views.html.index.render(webpage, filters.getFilters()));
    }

    /**
     * Application page where we find information about it
     * @return html result
     */
    public F.Promise<Result> details(String id)
            throws NoAppFoundException, ServerOverloadedException
    {
        WebPage webpage = new WebPage(session());

        // Add one view to the app
        wb.markViewed(id);

        F.Promise<AndroidApp> promise = wb.getAppDetails(id, Constants.Weight.FULL);
        F.Promise<Map<Integer, List<String>>> topics = wb.getTopicTitles();

        return promise.flatMap(app -> {

            List<String> ids = new LinkedList<>(app.getRelatedIDs());
            if (ids.size() > 20) {
                ids = ids.subList(0, 10);
            }

            return wb.getAppDetails(ids, Constants.Weight.LIGHT).flatMap(apps -> {
                return topics.map(titles -> {

                    webpage.addPath(routes.Application.index(), app.getCategory().getTitle());
                    webpage.addPath(routes.Application.details(app.getAppID()), app.getName());

                    return ok((play.twirl.api.Html) views.html.details.render(webpage, app, titles, apps));
                });
            });
        });
    }

}
