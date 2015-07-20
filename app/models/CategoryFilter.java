package models;

import constants.Constants.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaylor on 20.07.15.
 *
 */
public class CategoryFilter {

    private List<CategorySet> filters;

    public CategoryFilter() {
        filters = new ArrayList<>();

        /** Games **/
        CategorySet games = new CategorySet("Games");

        games.addAll(new Category[] {
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
        });
        filters.add(games);

        /** Social **/
        CategorySet social = new CategorySet("Social");

        social.addAll(new Category[]{
                Category.COMMUNICATION,
                Category.SOCIAL
        });
        filters.add(social);

        /** Entertainment **/
        CategorySet entertainment = new CategorySet("Entertainment");

        entertainment.addAll(new Category[] {
                Category.MUSIC_AND_AUDIO,
                Category.ENTERTAINMENT,
                Category.COMICS,
                Category.BOOKS_AND_REFERENCE,
                Category.PHOTOGRAPHY,
                Category.SHOPPING
        });
        filters.add(entertainment);

        /** Tools **/
        CategorySet tools = new CategorySet("Tools");

        tools.addAll(new Category[]{
                Category.TOOLS,
                Category.PERSONALIZATION
        });
        filters.add(tools);

        /** WORK **/
        CategorySet work = new CategorySet("Work");

        work.addAll(new Category[] {
                Category.BUSINESS,
                Category.EDUCATION,
                Category.FINANCE,
                Category.PRODUCTIVITY,
                Category.TRANSPORTATION,
                Category.TRAVEL_AND_LOCAL
        });
        filters.add(work);

        /** HEALTH **/
        CategorySet health = new CategorySet("Health");

        health.addAll(new Category[] {
                Category.HEALTH_AND_FITNESS,
                Category.SPORTS,
                Category.MEDICAL
        });
        filters.add(health);

        /** Others **/
        CategorySet others = new CategorySet("Others");
        others.addAll(new Category[] {
                Category.NEWS_AND_MAGAZINES,
                Category.WEATHER,
                Category.LIFESTYLE
        });
        filters.add(others);
    }

    public List<CategorySet> getFilters() {
        return filters;
    }
}
