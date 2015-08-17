package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import constants.Constants;
import models.AndroidApp;
import models.Review;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import webservice.WebService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gaylor on 13.07.15.
 * AJAX command for the website
 */
public class AJAX extends Controller {

    @Inject
    private WebService wb;

    /**
     * Return a JSON format of the reviews of the application
     * @return JSON
     */
    public F.Promise<Result> getReviews() {

        final String appID = request().getQueryString("id");

        F.Promise<List<Review>> promise = wb.getReviews(appID);
        return promise.map(reviews -> {

            if (request().getQueryString("admin") == null) {
                ArrayNode root = Json.newArray();

                for (Review review : reviews) {

                    if (review.parsed && review.opinions != null && root.size() < 50) {

                        root.add(views.html.templates.review.render(review, Login.getLocalUser(session())).toString());
                    }
                }

                return ok(root);
            } else {
                ArrayNode root = Json.newArray();

                for (Review review : reviews) {
                    if (review.parsed && review.parsedBody.size() > 0 && review.reviewBody.length() > 15) {
                        root.add(views.html.templates.admin_review.render(review).toString());
                    }
                }

                return ok(root);
            }
        });
    }

    /**
     * Execute a search for the given query
     * @return Json array of app template
     */
    public F.Promise<Result> search() {

        String name = request().getQueryString("query");

        return wb.search(name, manageCategories(request())).map(applications -> {

            ArrayNode root = Json.newArray();
            for (AndroidApp app : applications) {
                root.add(views.html.templates.play_app.render(app).toString());
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

            for (Integer index : indexes) {
                root.add(views.html.templates.play_app.render(applications.get(index)).toString());
            }

            return ok(root);
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
