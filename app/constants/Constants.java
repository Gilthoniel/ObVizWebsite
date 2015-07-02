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
}
