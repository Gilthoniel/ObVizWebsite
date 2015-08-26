package controllers;

import constants.Constants;
import models.AndroidApp;
import models.WebPage;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import service.TopicsManager;
import webservice.WebService;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

public class Application extends Controller {

    @Inject
    private WebService wb;
    @Inject
    private TopicsManager topics;

    /**
     * Homepage
     * @return html result
     */
    public Result index() {
        WebPage webpage = new WebPage(session());

        return ok((play.twirl.api.Html) views.html.index.render(webpage));
    }

    public Result contact() {
        WebPage webpage = new WebPage(session());

        return ok((play.twirl.api.Html) views.html.contact.render(webpage));
    }

    public Result disclaimer() {
        WebPage webpage = new WebPage(session());

        return ok((play.twirl.api.Html) views.html.disclaimer.render(webpage));
    }

    /**
     * Application page where we find information about it
     * @return html result
     */
    public F.Promise<Result> details(String id) {
        final WebPage webpage = new WebPage(session());

        // Add one view to the app
        wb.markViewed(id);

        F.Promise<AndroidApp> promise = wb.getAppDetails(id, Constants.Weight.LIGHT);

        return promise.flatMap(app -> {

            if (app == null) {
                return F.Promise.pure(notFound(
                                (play.twirl.api.Html) views.html.error.render(webpage, Constants.APP_NOT_FOUND))
                );
            }

            List<String> ids = new LinkedList<>(app.getRelatedIDs());
            if (ids.size() > 20) {
                ids = ids.subList(0, 10);
            }

            return wb.getAppDetails(ids, Constants.Weight.LIGHT).map(apps -> {

                webpage.addPath(routes.Application.details(app.getAppID()), app.getName());
                return ok((play.twirl.api.Html) views.html.details.render(webpage, app, topics, apps));
            });
        });
    }

}
