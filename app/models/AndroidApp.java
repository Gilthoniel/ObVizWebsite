package models;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import service.TopicsManager;
import webservice.MessageParser;

import java.io.Serializable;
import java.util.*;

/**
 * Created by gaylor on 29.06.15.
 * Represent a Google Play Application for Android device
 */
public class AndroidApp implements Serializable {

    private static final long serialVersionUID = 1820727929435164026L;

    private String appID;
    private String category;
    private String coverImgUrl;
    private String currentVersion;
    private String description;
    private String developer;
    private String installations;
    private boolean isFree;
    private String price;
    private String minimumOSVersion;
    private String name;
    private Date publicationDate;
    private List<String> screenshots;
    private Score score;
    private List<String> relatedIDs;
    private List<OpinionValue> opinionsSummary;
    private int nbParsedReviews;

    private boolean isOpinionsComputed = false;

    public AndroidApp(JsonObject json, MessageParser parser, TopicsManager topics) {
        appID = json.get("appID").getAsString();
        category = json.get("category").getAsString();
        coverImgUrl = json.get("coverImgUrl").getAsString();
        currentVersion = json.get("currentVersion").getAsString();
        description = json.get("description").getAsString();
        developer = json.get("developer").getAsString();
        installations = json.get("installations").getAsString();
        isFree = json.get("isFree").getAsBoolean();
        price = json.get("price").getAsString();
        minimumOSVersion = json.get("minimumOSVersion").getAsString();
        name = json.get("name").getAsString();
        publicationDate = parser.fromJson(json.get("publicationDate"), Date.class);
        screenshots = parser.fromJson(json.get("screenshots"), new TypeToken<List<String>>(){}.getType());
        score = parser.fromJson(json.get("score"), Score.class);
        relatedIDs = parser.fromJson(json.get("relatedIDs"), new TypeToken<List<String>>(){}.getType());
        opinionsSummary = parser.fromJson(json.get("opinionsSummary"), new TypeToken<List<OpinionValue>>(){}.getType());
        nbParsedReviews = json.get("nbParsedReviews").getAsInt();

        //* Remove topics without opinions
        if (opinionsSummary != null) {
            Iterator<OpinionValue> it = opinionsSummary.iterator();
            while (it.hasNext()) {
                OpinionValue opinion = it.next();

                if (!opinion.isValid()) {
                    it.remove();
                } else {
                    opinion.compute(topics, nbParsedReviews);
                }
            }

            Collections.sort(opinionsSummary, (opinion1, opinion2) ->
                    Integer.compare(opinion1.percentage(), opinion2.percentage()));
            Collections.reverse(opinionsSummary);
        }
        // */
    }

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
    public String getCategory() {

        return category;
    }

    /**
     * URL of the logo image
     * @return String
     */
    public String getImage() {
        return coverImgUrl.replaceFirst("w[0-9]{1,3}$", "w100");
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

    public String getDeveloper() {
        return developer;
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

    public String getPrice() {
        return price;
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
    public Date getPublicationDate() {

        return publicationDate;
    }

    public boolean isParsed() {

        return opinionsSummary != null;
    }

    /**
     * Return the list of URLs of the screenshots
     * @return List<String>
     */
    public List<String> getScreenshots() {
        return screenshots;
    }

    public String getRandomScreen(int size) {

        if (screenshots != null && screenshots.size() > 0) {
            Random random = new Random();
            return screenshots.get(random.nextInt(screenshots.size())).replaceFirst("[hw][0-9]+$", "w" + size);
        } else {

            return "";
        }
    }

    /**
     * Get the closest score in Integer format
     * @return total score of the application
     */
    public int getScore() {
        return (int) Math.round(score.getTotal());
    }

    public List<String> getRelatedIDs() {

        if (relatedIDs != null) {
            return relatedIDs;
        } else {
            return Collections.emptyList();
        }
    }

    public List<OpinionValue> getOpinions() {

        if (opinionsSummary != null) {

            return opinionsSummary;
        } else {

            return Collections.emptyList();
        }
    }

    public int getNbParsedReviews() {

        return nbParsedReviews;
    }

    public int getMostPositive() {

        return opinionsSummary.get(0).getTopicID();
    }

    public int getMostNegative() {
        return opinionsSummary.get(opinionsSummary.size() - 1).getTopicID();
    }

    public List<OpinionValue> getMostImportantTopics(TopicsManager manager) {

        List<OpinionValue> opinions = new ArrayList<>(getOpinions());
        Collections.sort(opinions);

        return opinions;
    }

    public int getGlobalOpinion() {

        int totalPositive = 0;
        int totalNegative = 0;

        for (OpinionValue value : getOpinions()) {
            totalPositive += value.getNumberPositive();
            totalNegative += value.getNumberNegative();
        }

        if (totalPositive <= 0 && totalNegative <= 0) {
            return -1;
        }

        return totalPositive * 100 / (totalPositive + totalNegative);
    }

    public class Pager {
        public List<AndroidApp> apps;
        public int nbTotalPages;
    }
}
