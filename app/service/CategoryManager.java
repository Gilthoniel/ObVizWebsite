package service;

import constants.Constants;
import models.CategorySet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaylor on 06-Aug-15.
 * List of set of categories
 */
public class CategoryManager {

    public static final CategoryManager instance = new CategoryManager();

    private List<CategorySet> sets;

    private CategoryManager() {
        sets = new ArrayList<>();

        /** All **/
        CategorySet all = new CategorySet("All");
        sets.add(all);

        /** Games **/
        CategorySet games = new CategorySet("Games");

        games.addAll(new Constants.Category[] {
                Constants.Category.GAME_ACTION,
                Constants.Category.GAME_ADVENTURE,
                Constants.Category.GAME_ARCADE,
                Constants.Category.GAME_BOARD,
                Constants.Category.GAME_CARD,
                Constants.Category.GAME_CASINO,
                Constants.Category.GAME_CASUAL,
                Constants.Category.GAME_EDUCATIONAL,
                Constants.Category.GAME_MUSIC,
                Constants.Category.GAME_PUZZLE,
                Constants.Category.GAME_RACING,
                Constants.Category.GAME_ROLE_PLAYING,
                Constants.Category.GAME_SIMULATION,
                Constants.Category.GAME_SPORTS,
                Constants.Category.GAME_STRATEGY,
                Constants.Category.GAME_TRIVIA,
                Constants.Category.GAME_WORD
        });
        sets.add(games);

        /** Social **/
        CategorySet social = new CategorySet("Social");

        social.addAll(new Constants.Category[]{
                Constants.Category.COMMUNICATION,
                Constants.Category.SOCIAL
        });
        sets.add(social);

        /** Entertainment **/
        CategorySet entertainment = new CategorySet("Entertainment");

        entertainment.addAll(new Constants.Category[] {
                Constants.Category.MUSIC_AND_AUDIO,
                Constants.Category.ENTERTAINMENT,
                Constants.Category.COMICS,
                Constants.Category.BOOKS_AND_REFERENCE,
                Constants.Category.PHOTOGRAPHY,
                Constants.Category.SHOPPING
        });
        sets.add(entertainment);

        /** Tools **/
        CategorySet tools = new CategorySet("Tools");

        tools.addAll(new Constants.Category[]{
                Constants.Category.TOOLS,
                Constants.Category.PERSONALIZATION
        });
        sets.add(tools);

        /** WORK **/
        CategorySet work = new CategorySet("Work");

        work.addAll(new Constants.Category[] {
                Constants.Category.BUSINESS,
                Constants.Category.EDUCATION,
                Constants.Category.FINANCE,
                Constants.Category.PRODUCTIVITY,
                Constants.Category.TRANSPORTATION,
                Constants.Category.TRAVEL_AND_LOCAL
        });
        sets.add(work);

        /** HEALTH **/
        CategorySet health = new CategorySet("Health");

        health.addAll(new Constants.Category[] {
                Constants.Category.HEALTH_AND_FITNESS,
                Constants.Category.SPORTS,
                Constants.Category.MEDICAL
        });
        sets.add(health);

        /** Others **/
        CategorySet others = new CategorySet("Others");
        others.addAll(new Constants.Category[] {
                Constants.Category.NEWS_AND_MAGAZINES,
                Constants.Category.WEATHER,
                Constants.Category.LIFESTYLE
        });
        sets.add(others);
    }

    public List<CategorySet> getCategories() {

        return sets;
    }
}
