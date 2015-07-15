package controllers;

import constants.Constants;
import constants.Constants.Category;
import models.AndroidApp;
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

    public Application() {

        wb = WebService.getInstance();
    }

    /**
     * Homepage
     * @return html result
     */
    public Result index() {
        WebPage webpage = new WebPage();

        Category[] categories = new Category[] {
                Category.COMMUNICATION,
                Category.SOCIAL,
                Category.MUSIC_AND_AUDIO,
                Category.ENTERTAINMENT,
                Category.COMICS,
                Category.BOOKS_AND_REFERENCE,
                Category.PHOTOGRAPHY,
                Category.SHOPPING,
                Category.TOOLS,
                Category.PERSONALIZATION,
                Category.BUSINESS,
                Category.EDUCATION,
                Category.FINANCE,
                Category.PRODUCTIVITY,
                Category.TRANSPORTATION,
                Category.TRAVEL_AND_LOCAL,
                Category.HEALTH_AND_FITNESS,
                Category.SPORTS,
                Category.MEDICAL,
                Category.NEWS_AND_MAGAZINES,
                Category.WEATHER,
                Category.LIFESTYLE
        };

        Category[] games = new Category[] {
                Category.GAME_ACTION,
                Category.GAME_ADVENTURE,
                Category.GAME_ARCADE,
                Category.GAME_BOARD,
                Category.GAME_CARD,
                Category.GAME_CASINO,
                Category.GAME_CASUAL,
                Category.GAME_EDUCATIONAL,
                Category.GAME_MUSIC,
                Category.GAME_PUZZLE,
                Category.GAME_RACING,
                Category.GAME_ROLE_PLAYING,
                Category.GAME_SIMULATION,
                Category.GAME_SPORTS,
                Category.GAME_STRATEGY,
                Category.GAME_TRIVIA,
                Category.GAME_WORD
        };

        return ok((play.twirl.api.Html) views.html.index.render(webpage, categories, games));
    }

    /**
     * Application page where we find information about it
     * @return html result
     */
    public F.Promise<Result> details(String id)
            throws NoAppFoundException, ServerOverloadedException
    {
        WebPage webpage = new WebPage();

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
