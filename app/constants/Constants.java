package constants;

/**
 * Created by gaylor on 25.06.15.
 * List of parameter constants
 */
public class Constants {

    /* Cache parameters */
    public static int TIME_CACHE_EXPIRED = 300; // 5min

    /* WebService parameters */
    public static final String baseURL = "http://vps40100.vps.ovh.ca/ObVizServiceAdmin";

    /* Command builder */
    public enum Weight {LIGHT, MEDIUM, FULL}

    public static final String GET_APP_IDS = "Get_App_IDs";
    public static final String GET_APP = "Get_App";
    public static final String GET_TOPIC_TITLES = "Get_App_Topics";

    /* Category */

    public enum Category {
        SOCIAL("social", "Social"),
        GAME_ADVENTURE("games", "Adventure Games"),
        GAME_SPORTS("games", "Sport Games"),
        GAME_STRATEGY("games", "Strategy Games"),
        GAME_ACTION("games", "Action Games"),
        GAME_PUZZLE("games", "Puzzle Games"),
        GAME_ARCADE("games", "Arcade Games"),
        GAME_CARD("games", "Card Games"),
        GAME_CASUAL("games", "Casual Games"),
        GAME_CASINO("games", "Casino Games"),
        GAME_TRIVIA("games", "Trivia Games"),
        GAME_SIMULATION("games", "Simulation Games"),
        GAME_RACING("games", "Racing Games"),
        GAME_ROLE_PLAYING("games", "RPG Games"),
        GAME_WORD("games", "Word Games"),
        GAME_BOARD("games", "Board Games"),
        GAME_EDUCATIONAL("games", "Educational Games"),
        GAME_MUSIC("games", "Music Games"),
        COMMUNICATION("communication", "Communication"),
        MUSIC_AND_AUDIO("audio", "Music and Audio"),
        ENTERTAINMENT("entertainment", "Entertainment"),
        TOOLS("tools", "Tools"),
        BOOKS_AND_REFERENCE("books", "Books"),
        PERSONALIZATION("personalization", "Personalization"),
        PRODUCTIVITY("productivity", "Productivity"),
        WEATHER("weather", "Weather"),
        SHOPPING("shopping", "Shopping"),
        TRANSPORTATION("transportation", "Transportation"),
        LIFESTYLE("lifestyle", "Lifestyle"),
        TRAVEL_AND_LOCAL("travel", "Travel and Local"),
        PHOTOGRAPHY("photo", "Photography"),
        BUSINESS("business", "Business"),
        HEALTH_AND_FITNESS("health", "Health"),
        FINANCE("finance", "Finance"),
        SPORTS("sports", "Sports"),
        EDUCATION("education", "Education"),
        NEWS_AND_MAGAZINES("news", "News and Magazines"),
        MEDICAL("medical", "Medical"),
        COMICS("comics", "Comics"),
        DEFAULT("default", "Application");

        private final String name;
        private final String title;
        Category(String value, String title) {
            name = value;
            this.title = title;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }
    }

    /* Exception */

    public static final String NO_APP_EXCEPTION = "NO_APP";
    public static final String SERVER_OVERLOADED_EXCEPTION = "SERVER_OVERLOADED";
}
