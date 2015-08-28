package models;

import service.TopicsManager;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

/**
 * Created by gaylor on 29.06.15.
 * Represent a Google Play Application for Android device
 */
public class AndroidApp implements Initiatable, Serializable {

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

    @Override
    public void init() {

        //* Remove topics without opinions
        if (opinionsSummary != null) {
            Iterator<OpinionValue> it = opinionsSummary.iterator();
            while (it.hasNext()) {
                if (!it.next().isValid()) {
                    it.remove();
                }
            }

            Collections.sort(opinionsSummary, (opinion1, opinion2) ->
                    Integer.compare(opinion2.getNumberPositive() - opinion2.getNumberNegative(),
                                        opinion1.getNumberPositive() - opinion1.getNumberNegative()));
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

    public List<OpinionValue> getOpinions(@Nullable TopicsManager manager) {

        if (opinionsSummary != null) {

            if (manager != null && !isOpinionsComputed) {
                for (OpinionValue opinion : opinionsSummary) {
                    opinion.compute(manager, nbParsedReviews);
                }
            }

            return opinionsSummary;
        } else {
            return Collections.emptyList();
        }
    }

    public List<OpinionValue> getOpinions() {

        return getOpinions(null);
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

        List<OpinionValue> opinions = new ArrayList<>(getOpinions(manager));
        Collections.sort(opinions);

        return opinions;
    }

    public int getGlobalOpinion() {

        int totalPositive = 0;
        int totalNegative = 0;

        for (OpinionValue value : getOpinions(null)) {
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
