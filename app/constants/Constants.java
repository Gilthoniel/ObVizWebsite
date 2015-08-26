package constants;

/**
 * Created by gaylor on 25.06.15.
 * List of parameter constants
 */
public class Constants {

    /* ERROR'S MESSAGE */
    public static final String APP_NOT_FOUND = "Sorry, we can't find this. You should try to use the search bar!";

    /* Cache parameters */
    public static int TIME_CACHE_EXPIRED = 60 * 60 * 1000; // in ms

    /* WebService parameters */
    public static final String baseURL = "http://vps186042.ovh.net/ObVizService";
    public static final String adminURL = "http://vps186042.ovh.net/ObVizServiceAdmin";
    public static final int TIMEOUT = 20000;

    public static final int NUMBER_PARSED_APP_PER_PAGE = 20;

    public static final int NUMBER_TRENDING_APPS = 21;

    /* Command builder */
    public enum Weight {LIGHT, MEDIUM, FULL}

    public static final String GET_APP = "Get_App";
    public static final String GET_TOPIC_TITLES = "Get_App_Topics";
    public static final String GET_REVIEWS = "Get_Reviews";
    public static final String SEARCH_APPS = "Search_Apps";
    public static final String APP_VIEWED = "App_Viewed";
    public static final String GET_TRENDING_APPS = "Get_Trending_Apps";

    public static final String GET_PARSED_APP = "Get_Parsed_Apps";
    public static final String GET_APPS_CRAWLER_LOGS = "Get_Apps_Crawler_Logs";
    public static final String PROPOSE_ARGUMENT = "Propose_App_Argument";
    public static final String GET_ARGUMENTS = "Get_App_Arguments";
    public static final String GET_APP_TOPICS = "Get_App_Topics";
    public static final String UPDATE_TOPIC = "Insert_App_Topic";

    /* Machines */
    public static final String[] MACHINES = new String[] {
            "liapc3", "claudiu", "liapc4", "liapc13", "liapc55"
    };

    /* Category */

    public enum Category {
        SOCIAL("social", "Social"),
        ADVENTURE("games", "Adventure"),
        GAME_SPORTS("games", "Sport"),
        STRATEGY("games", "Strategy"),
        ACTION("games", "Action"),
        PUZZLE("games", "Puzzle"),
        ARCADE("games", "Arcade"),
        CARD("games", "Card"),
        CASUAL("games", "Casual"),
        CASINO("games", "Casino"),
        TRIVIA("games", "Trivia"),
        SIMULATION("games", "Simulation"),
        RACING("games", "Racing"),
        ROLE_PLAYING("games", "RPG"),
        WORD("games", "Word"),
        BOARD("games", "Board"),
        EDUCATIONAL("games", "Educational"),
        MUSIC("games", "Music"),
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
    public static final String AJAX_REQUEST_EXCEPTION = "AJAX_REQUEST_EXCEPTION";
}
