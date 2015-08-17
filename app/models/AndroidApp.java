package models;

import constants.Constants;
import play.Logger;

import java.io.Serializable;
import java.util.*;

/**
 * Created by gaylor on 29.06.15.
 * Represent a Google Play Application for Android device
 */
public class AndroidApp implements Initiatable, Serializable {

    private static final long serialVersionUID = 1820727929435164026L;
    private static Random random = new Random();

    private String appID;
    private Constants.Category category;
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
    private List<Review> reviews;
    private List<String> relatedUrls;
    private int nbParsedReviews;

    private Map<Integer, OpinionValue> mappedOpinions;
    private int maxNumberOpinions;
    private int totalPositive;
    private int totalNegative;

    @Override
    public void init() {

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
    public Constants.Category getCategory() {
        if (category != null) {
            return category;
        } else {
            return Constants.Category.DEFAULT;
        }
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

    public int getNbParsedReviews() {
        return nbParsedReviews;
    }

    public List<String> getRelatedIDs() {

        if (relatedUrls != null) {
            return relatedUrls;
        } else {
            return new ArrayList<>();
        }
    }

    public Map<Integer, OpinionValue> getMappedOpinions() {

        if (mappedOpinions == null) { // TODO : remove
            mappedOpinions = new HashMap<>();

            for (int i = 1; i < 10; i++) {

                OpinionValue opinion = new OpinionValue(i);
                opinion.setNegative(random.nextInt(300));
                opinion.setPositive(random.nextInt(300));

                mappedOpinions.put(i, opinion);
            }
        }

        return mappedOpinions;
    }

    public List<OpinionValue> getMostImportantTopics() {

        List<OpinionValue> opinions = new ArrayList<>(getMappedOpinions().values());
        Collections.sort(opinions);

        return opinions;
    }

    public int getMaxNumberOpinions() {
        return maxNumberOpinions;
    }

    public int getGlobalOpinion() {

        if (totalPositive > 0 && totalNegative > 0) {
            return Math.round(totalPositive * 100 / (totalNegative + totalPositive));
        } else {
            return 0;
        }
    }
}
