package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import models.AndroidApp;
import models.Review;
import models.errors.AJAXRequestException;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import webservice.MessageParser;
import service.TopicsManager;
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
    @Inject
    private TopicsManager topics;

    /**
     * Return a JSON format of the reviews of the application
     * @return JSON
     */
    public F.Promise<Result> getReviews() throws AJAXRequestException {

        final String appID = request().getQueryString("id");
        final int topicID = MessageParser.parseInt(request().getQueryString("t"));
        final int page = MessageParser.parseInt(request().getQueryString("p"));
        final int size = MessageParser.parseInt(request().getQueryString("s"));
        if (topicID < 0 || page < 0 || size < 0) {
            throw new AJAXRequestException();
        }

        F.Promise<Review.ReviewContainer> promise = wb.getReviews(appID, topicID, page, size);
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
            ArrayNode root = Json.newArray();
            for (AndroidApp app : applications) {
                root.add(views.html.templates.play_app.render(app, topics, "chart-search").toString());
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
                root.add(views.html.templates.play_app.render(applications.get(index), topics, "chart-trending").toString());
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
