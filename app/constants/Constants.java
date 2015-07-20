package constants;

import play.api.Play;

/**
 * Created by gaylor on 25.06.15.
 * List of parameter constants
 */
public class Constants {

    /* Cache parameters */
    public static int TIME_CACHE_EXPIRED = 300; // 5min

    /* WebService parameters */
    public static final String baseURL = "http://vps186042.ovh.net/ObVizService";
    public static final String adminURL = "http://vps186042.ovh.net/ObVizServiceAdmin";
    public static final long TIMEOUT = (Long) Play.current().configuration().getLong("play.ws.timeout.request").get(); // in millisecond

    /* Command builder */
    public enum Weight {LIGHT, MEDIUM, FULL}

    public static final String GET_APP = "Get_App";
    public static final String GET_TOPIC_TITLES = "Get_App_Topics";
    public static final String GET_REVIEWS = "Get_Reviews";
    public static final String SEARCH_APPS = "Search_Apps";

    public static final String GET_APPS_CRAWLER_LOGS = "Get_Apps_Crawler_Logs";
    public static final String APP_VIEWED = "App_Viewed";

    /* Machines */
    public static final String[] MACHINES = new String[] {
            "liapc3", "claudiu", "liapc4", "liapc13", "liapc55"
    };

    /* Category */

    public enum Category {
        SOCIAL("social", "Social"),
        GAME_ADVENTURE("games", "Adventure"),
        GAME_SPORTS("games", "Sport"),
        GAME_STRATEGY("games", "Strategy"),
        GAME_ACTION("games", "Action"),
        GAME_PUZZLE("games", "Puzzle"),
        GAME_ARCADE("games", "Arcade"),
        GAME_CARD("games", "Card"),
        GAME_CASUAL("games", "Casual"),
        GAME_CASINO("games", "Casino"),
        GAME_TRIVIA("games", "Trivia"),
        GAME_SIMULATION("games", "Simulation"),
        GAME_RACING("games", "Racing"),
        GAME_ROLE_PLAYING("games", "RPG"),
        GAME_WORD("games", "Word"),
        GAME_BOARD("games", "Board"),
        GAME_EDUCATIONAL("games", "Educational"),
        GAME_MUSIC("games", "Music"),
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
