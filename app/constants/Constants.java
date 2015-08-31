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

    public static final int NUMBER_TRENDING_APPS = 20;

    /* Command builder */
    public enum Weight {LIGHT, MEDIUM, FULL}

    public static final String GET_APP = "Get_App";
    public static final String GET_TOPIC_TITLES = "Get_App_Topics";
    public static final String GET_REVIEWS = "Get_Reviews";
    public static final String SEARCH_APPS = "Search_Apps";
    public static final String APP_VIEWED = "App_Viewed";
    public static final String GET_TRENDING_APPS = "Get_Trending_Apps";
    public static final String GET_CATEGORIES = "Get_App_Categories";
    public static final String GET_CATEGORIES_TYPES = "Get_App_Categories_Types";

    public static final String GET_PARSED_APP = "Get_Parsed_Apps";
    public static final String GET_APPS_CRAWLER_LOGS = "Get_Apps_Crawler_Logs";
    public static final String PROPOSE_ARGUMENT = "Propose_App_Argument";
    public static final String GET_ARGUMENTS = "Get_App_Arguments";
    public static final String GET_APP_TOPICS = "Get_App_Topics";
    public static final String UPDATE_TOPIC = "Insert_App_Topic";
    public static final String INSERT_TYPE = "Insert_App_Category_Type";
    public static final String UPDATE_CATEGORY = "Update_App_Category";

    /* Machines */
    public static final String[] MACHINES = new String[] {
            "liapc3", "claudiu", "liapc4", "liapc13", "liapc55"
    };

    /* Exception */

    public static final String AJAX_REQUEST_EXCEPTION = "AJAX_REQUEST_EXCEPTION";
}
