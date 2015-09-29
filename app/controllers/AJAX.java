package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import constants.Utils;
import models.*;
import models.errors.AJAXRequestException;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import webservice.WebService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gaylor on 13.07.15.
 * AJAX command for the website
 */
@With(WebPageInformation.class)
public class AJAX extends Controller {

    @Inject
    private WebService wb;

    /**
     * Return a JSON format of the reviews of the application
     * @return JSON
     */
    public F.Promise<Result> getReviews() throws AJAXRequestException {

        final String appID = request().getQueryString("id");
        final int topicID = Utils.parseInt(request().getQueryString("t"));
        final int page = Utils.parseInt(request().getQueryString("p"));
        final int size = Utils.parseInt(request().getQueryString("s"));
        if (topicID < 0 || page < 0 || size < 0) {
            throw new AJAXRequestException();
        }
        String order = request().getQueryString("order");
        if (order == null) {
            order = "RANDOM";
        }

        F.Promise<Review.ReviewContainer> promise = wb.getReviews(appID, topicID, page, size, order);
        return promise.map(container -> {
            ObjectNode root = Json.newObject();

            ArrayNode reviews = root.putArray("reviews");
            for (Review review : container.reviews) {
                if (review.parsed && review.opinions != null && root.size() < 50) {

                    reviews.add(views.html.templates.review.render(review, Login.getLocalUser(session())).toString());
                }
            }

            root.put("nbPage", container.nbTotalPages);
            return ok(root);
        });
    }

    /**
     * Execute a search for the given query
     * @return Json array of app template
     */
    public F.Promise<Result> search() {

        String name = request().getQueryString("query");

        return wb.search(name, manageCategories(request())).map(applications -> {
            WebPage webpage = (WebPage) Http.Context.current().args.get("com.obviz.webpage");

            ArrayNode root = Json.newArray();
            Iterator<AndroidApp> it = applications.iterator();
            while (it.hasNext() && root.size() < Constants.NUMBER_RESULT_SEARCH) {
                root.add(views.html.templates.play_app.render(it.next(), webpage).toString());
            }

            return ok(root);
        });
    }

    /**
     * Execute a discover search for the given queries
     * @return Json array of discover items
     */
    public F.Promise<Result> discover() {

        String name = request().getQueryString("query");

        return wb.discover(name, manageCategories(request())).map(items -> {
            WebPage webpage = (WebPage) Http.Context.current().args.get("com.obviz.webpage");

            ArrayNode root = Json.newArray();
            for (DiscoverItem item : items) {
                root.add(views.html.templates.discover_item.render(item, webpage).toString());
            }

            return ok(root);
        });
    }

    /**
     * Get the list of trending apps and return them in an Html template
     * @return Json array of Html template
     */
    public F.Promise<Result> getTrending() {

        return wb.getTrending(manageCategories(request())).map(applications -> {
            ArrayNode root = Json.newArray();

            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < applications.size(); i++) {
                indexes.add(i);
            }
            Collections.shuffle(indexes);

            if (indexes.size() > Constants.NUMBER_TRENDING_APPS) {
                indexes = indexes.subList(0, Constants.NUMBER_TRENDING_APPS);
            }

            WebPage webpage = (WebPage) Http.Context.current().args.get("com.obviz.webpage");
            for (Integer index : indexes) {
                root.add(views.html.templates.play_app.render(applications.get(index), webpage).toString());
            }

            return ok(root);
        });
    }

    public F.Promise<Result> getAlternativesHeadline() {

        String appID = request().getQueryString("id");
        int topicID = Utils.parseInt(request().getQueryString("topic"));

        return wb.getAppDetails(appID, Constants.Weight.LIGHT).flatMap(app -> {
            final OpinionValue opinion = app.getOpinion(topicID);

            return wb.getAppDetails(app.getAlternativeApps(), Constants.Weight.LIGHT).map(alternatives -> {

                ArrayNode root = Json.newArray();
                for (AndroidApp alt : alternatives) {

                    OpinionValue other = alt.getOpinion(topicID);
                    if (other != null && other.percentage() > opinion.percentage()) {
                        root.add(views.html.templates.alternative_body.render(alt).toString());
                    }
                }

                return ok(root);
            });
        });
    }

    /* PRIVATE */

    /**
     * Parse the string of categories into a list of categories
     * @param request Http request
     * @return the list
     */
    private List<String> manageCategories(Http.Request request) {
        List<String> categories = new ArrayList<>();
        String query = request.getQueryString("categories");

        if (query != null && !query.isEmpty()) {
            Collections.addAll(categories, query.split(","));
        }

        return categories;
    }
}
