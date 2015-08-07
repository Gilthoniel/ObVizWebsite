package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import constants.Constants;
import models.AndroidApp;
import models.Opinion;
import models.OpinionValue;
import models.Review;
import play.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import webservice.MessageParser;
import webservice.WebService;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by gaylor on 13.07.15.
 * AJAX command for the website
 */
public class AJAX extends Controller {

    @Inject
    private WebService wb;

    /**
     * Return a JSON format of the reviews of the application
     * @param appID ID of the application
     * @return JSON
     */
    public F.Promise<Result> getReviews(String appID) {

        F.Promise<List<Review>> promise = wb.getReviews(appID);
        return promise.map(reviews -> {

            if (request().getQueryString("admin") == null) {
                ObjectNode root = Json.newObject();

                for (Review review : reviews) {

                    if (review.getOpinions() != null) {
                        for (Integer topicID : review.getOpinions().keySet()) {

                            String key = String.valueOf(topicID);
                            if (root.get(key) == null) {
                                root.set(key, Json.newArray());
                            }

                            ((ArrayNode) root.get(key)).add(views.html.templates.review.render(review, topicID).toString());
                        }
                    }
                }

                return ok(root);
            } else {
                ArrayNode root = Json.newArray();

                for (Review review : reviews) {
                    if (review.getBodySentences().size() > 0 && review.getReviewBody().length() > 15) {
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
