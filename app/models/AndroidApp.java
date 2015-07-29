package models;

import constants.Constants;
import play.Logger;

import java.util.*;

/**
 * Created by gaylor on 29.06.15.
 * Represent a Google Play Application for Android device
 */
public class AndroidApp implements Initiatable {

    private static Random random = new Random();

    private String appID;
    private Constants.Category category;
    private String coverImgURL;
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

    private Map<Integer, Set<Review>> mappedReviews;
    private Map<Integer, OpinionValue> mappedOpinions;
    private int maxNumberOpinions;
    private int totalPositive;
    private int totalNegative;

    @Override
    public void init() {
        mappedReviews = new TreeMap<>();
        mappedOpinions = new TreeMap<>();

        if (reviews == null) {
            return;
        }

        for (Review review : reviews) {

            if (review.getOpinions() != null) {

                review.getOpinions().forEach((topicID, opinions) -> {
                    // Add the review for this topic ID
                    if (!mappedReviews.containsKey(topicID)) {
                        mappedReviews.put(topicID, new HashSet<>());
                    }

                    mappedReviews.get(topicID).add(review);

                    // Update the number of opinions
                    if (!mappedOpinions.containsKey(topicID)) {
                        mappedOpinions.put(topicID, new OpinionValue(topicID));
                    }

                    OpinionValue opinionValue = mappedOpinions.get(topicID);
                    for (Opinion opinion : opinions) {
                        if (opinion.getPolarity() == Opinion.Polarity.POSITIVE) {
                            opinionValue.addPositives(1);
                        } else {
                            opinionValue.addNegatives(1);
                        }
                    }
                });
            }
        }

        maxNumberOpinions = 0;
        mappedOpinions.values().stream().forEach(opinionValue -> {

            if (opinionValue.getTotal() > maxNumberOpinions) {
                maxNumberOpinions = opinionValue.getTotal();
            }

            totalPositive += opinionValue.getNumberPositive();
            totalNegative += opinionValue.getNumberNegative();
        });
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

    public Map<Integer, Set<Review>> getMappedReviews() {

        if (mappedReviews == null) {
            throw new UnsupportedOperationException("Should be initialize");
        }

        return mappedReviews;
    }

    public Map<Integer, OpinionValue> getMappedOpinions() {

        if (mappedOpinions == null) {
            throw new UnsupportedOperationException("Should be initialize");
        } else if (mappedOpinions.isEmpty()) { // TODO : remove

            for (int i = 1; i <= 10; i++) {

                OpinionValue opinion = new OpinionValue(i);
                opinion.setNegative(random.nextInt(300));
                opinion.setPositive(random.nextInt(300));

                mappedOpinions.put(i, opinion);
            }
        }

        return mappedOpinions;
    }

    public List<OpinionValue> getMostImportantTopics() {

        List<OpinionValue> opinions = new ArrayList<>(mappedOpinions.values());
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
