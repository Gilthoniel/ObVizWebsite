package models;

import controllers.routes;

import java.util.List;

/**
 * Created by gaylor on 29.06.15.
 * Represent a Google Play Application for Android device
 */
public class PlayApp {

    public enum Category {
        SOCIAL("social"),
        GAME_ADVENTURE("games"),
        GAME_SPORTS("games"),
        GAME_STRATEGY("games"),
        GAME_ACTION("games"),
        GAME_PUZZLE("games"),
        GAME_ARCADE("games"),
        GAME_CARD("games"),
        GAME_CASUAL("games"),
        GAME_CASINO("games"),
        GAME_TRIVIA("games"),
        GAME_SIMULATION("games"),
        GAME_RACING("games"),
        GAME_ROLE_PLAYING("games"),
        GAME_WORD("games"),
        GAME_BOARD("games"),
        GAME_EDUCATIONAL("games"),
        GAME_MUSIC("games"),
        COMMUNICATION("communication"),
        DEFAULT("games");

        private final String name;
        Category(String value) {
            name = value;
        }

        public String getName() {
            return name;
        }
    }

    private String appID;
    private Category category;
    private String coverImgURL;
    private String currentVersion;
    private String description;
    private String installations;
    private boolean isFree;
    private String minimumOSVersion;
    private String name;
    private Date publicationDate;
    private List<String> screenshots;
    private Score score;

    /**
     * ID of the application
     * @return String
     */
    public String getAppID() {
        return appID;
    }

    /**
     * Category of the application
     * @return String
     */
    public Category getCategory() {
        if (category != null) {
            return category;
        } else {
            return Category.DEFAULT;
        }
    }

    /**
     * URL of the logo image
     * @return String
     */
    public String getImage() {
        return coverImgURL;
    }

    /**
     * Version of the application
     * @return String format
     */
    public String getVersion() {
        return currentVersion;
    }

    /**
     * Description of the application
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return the number of installations of the application
     * @return String format
     */
    public String getInstallations() {
        return installations;
    }

    /**
     * Return true if the application is free
     * @return true or false
     */
    public boolean isFree() {
        return isFree;
    }

    /**
     * Get the minimum version required for the app
     * @return String format
     */
    public String getMinOS() {
        return minimumOSVersion;
    }

    /**
     * The name of the application
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Date of the release of the app
     * @return Long timestamp format
     */
    public long getPublicationDate() {

        if (publicationDate != null) {
            return publicationDate.getValue();
        } else {
            return 0;
        }
    }

    /**
     * Return the list of URLs of the screenshots
     * @return List<String>
     */
    public List<String> getScreenshots() {
        return screenshots;
    }

    /**
     * Get the closest score in Integer format
     * @return total score of the application
     */
    public int getScore() {
        return (int) Math.round(score.getTotal());
    }
}
