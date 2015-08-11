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
                Constants.Category.ACTION,
                Constants.Category.ADVENTURE,
                Constants.Category.ARCADE,
                Constants.Category.BOARD,
                Constants.Category.CARD,
                Constants.Category.CASINO,
                Constants.Category.CASUAL,
                Constants.Category.EDUCATIONAL,
                Constants.Category.MUSIC,
                Constants.Category.PUZZLE,
                Constants.Category.RACING,
                Constants.Category.ROLE_PLAYING,
                Constants.Category.SIMULATION,
                Constants.Category.GAME_SPORTS,
                Constants.Category.STRATEGY,
                Constants.Category.TRIVIA,
                Constants.Category.WORD
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
