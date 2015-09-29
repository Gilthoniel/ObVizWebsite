package controllers;

import com.google.inject.Inject;
import constants.Constants;
import models.AndroidApp;
import models.WebPage;
import play.Logger;
import play.data.DynamicForm;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import webservice.WebService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@With(WebPageInformation.class)
public class Application extends Controller {

    private WebService wb;

    @Inject
    public Application(WebService service) {
        wb = service;
    }

    /**
     * Homepage
     * @return html result
     */
    public F.Promise<Result> index() {
        WebPage webpage = getWebpage();
        Http.Cookie cookie = request().cookie(Constants.COOKIE_VIDEO);
        webpage.addParam("home-video", cookie != null ? "false" : "true");

        response().setCookie(Constants.COOKIE_VIDEO, "", 3600 * 24 * 365, "/");

        return wb.getHeadLine().map(headline -> {
            return ok((play.twirl.api.Html) views.html.index.render(webpage, headline));
        });
    }

    public Result contact() {

        return ok((play.twirl.api.Html) views.html.contact.render(getWebpage()));
    }

    public Result disclaimer() {

        return ok((play.twirl.api.Html) views.html.disclaimer.render(getWebpage()));
    }

    /**
     * Application page where we find information about it
     * @return html result
     */
    public F.Promise<Result> details(String id) {

        // Add one view to the app
        wb.markViewed(id);

        F.Promise<AndroidApp> promise = wb.getAppDetails(id, Constants.Weight.LIGHT);

        return promise.flatMap(app -> {

            if (app == null) {
                return F.Promise.pure(notFound(
                                (play.twirl.api.Html) views.html.error.render(getWebpage(), Constants.APP_NOT_FOUND))
                );
            }

            List<String> ids = new LinkedList<>(app.getAlternativeApps());
            if (ids.size() > 20) {
                ids = ids.subList(0, 10);
            }

            return wb.getAppDetails(ids, Constants.Weight.LIGHT).map(apps -> {

                WebPage webpage = getWebpage();
                webpage.addPath(routes.Application.details(app.getAppID()), app.getName(), true);
                return ok((play.twirl.api.Html) views.html.details.render(webpage, app, apps));
            });
        });
    }

    public Result controversies() {
        WebPage webpage = getWebpage();

        return ok((play.twirl.api.Html) views.html.controversies.render(webpage));
    }

    public F.Promise<Result> discover() {

        return wb.getTrending(Collections.<String>emptyList()).map(apps -> {
            return ok((play.twirl.api.Html) views.html.discover.render(getWebpage(), apps));
        });
    }

    public Result search(String query) {
        WebPage webpage = getWebpage();
        webpage.addPath(routes.Application.index(), "Home");
        webpage.addPath(null, "Search results");

        return ok((play.twirl.api.Html) views.html.search.render(webpage, query));
    }

    public Result postSearch() {

        DynamicForm form = DynamicForm.form().bindFromRequest();

        return redirect(routes.Application.search(form.get("query")));
    }

    private WebPage getWebpage() {

        return (WebPage) Http.Context.current().args.get("com.obviz.webpage");
    }

}
