package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import models.AndroidApp;
import models.Opinion;
import models.OpinionValue;
import models.Review;
import play.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import webservice.MessageParser;
import webservice.WebService;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by gaylor on 13.07.15.
 * AJAX command for the website
 */
public class AJAX extends Controller {

    private WebService wb;

    public AJAX() {
        wb = WebService.getInstance();
    }

    /**
     * Return a JSON format of the reviews of the application
     * @param appID ID of the application
     * @return JSON
     */
    public F.Promise<Result> getReviews(String appID) {

        F.Promise<List<Review>> promise = wb.getReviews(appID);
        return promise.map(reviews -> {

            if (request().getQueryString("admin") != null) {
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
                    if (review.getBodySentences().size() > 0) {
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

        String name = request().getQueryString("name");

        List<String> categories = new ArrayList<>();
        String query = request().getQueryString("categories");

        if (query != null && !query.isEmpty()) {
            Collections.addAll(categories, query.split(","));
        }

        F.Promise<List<AndroidApp>> promise = wb.search(name, categories);
        return promise.map(applications -> {

            ArrayNode root = Json.newArray();
            if (request().getQueryString("admin") == null) {
                for (AndroidApp app : applications) {
                    root.add(views.html.templates.play_app.render(app).toString());
                }
            } else {
                for (AndroidApp app : applications) {
                    root.add(views.html.templates.admin_play_app.render(app).toString());
                }
            }

            return ok(root);
        });
    }
}
