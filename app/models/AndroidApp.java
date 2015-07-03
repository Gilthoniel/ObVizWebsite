package models;

import constants.Constants;

import java.util.*;

/**
 * Created by gaylor on 29.06.15.
 * Represent a Google Play Application for Android device
 */
public class AndroidApp implements Initiatable {

    private String appID;
    private Constants.Category category;
    private String coverImgURL;
    private String currentVersion;
    private String description;
    private String installations;
    private boolean isFree;
    private String minimumOSVersion;
    private String name;
    //private Date publicationDate;
    private List<String> screenshots;
    private Score score;
    private List<Review> reviews;
    private List<String> relatedUrls;

    private Map<Integer, Set<Review>> mappedReviews;
    private Map<Integer, OpinionValue> mappedOpinions;

    @Override
    public void init() {
        mappedReviews = new TreeMap<>();
        mappedOpinions = new TreeMap<>();

        for (Review review : reviews) {

            if (review.getOpinions() != null) {
                for (Opinion opinion : review.getOpinions()) {

                    // Add the review for this topic ID
                    if (!mappedReviews.containsKey(opinion.getTopicID())) {
                        mappedReviews.put(opinion.getTopicID(), new HashSet<>());
                    }

                    mappedReviews.get(opinion.getTopicID()).add(review);

                    // Update the number of opinions
                    if (!mappedOpinions.containsKey(opinion.getTopicID())) {
                        mappedOpinions.put(opinion.getTopicID(), new OpinionValue());
                    }

                    for (Opinion.OpinionChild child : opinion.getChildren()) {
                        if (child.getPolarity() == Opinion.Polarity.POSITIVE) {
                            mappedOpinions.get(opinion.getTopicID()).addPositives(1);
                        } else {
                            mappedOpinions.get(opinion.getTopicID()).addNegatives(1);
                        }
                    }
                }
            }
        }
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
     *
    public long getPublicationDate() {

        if (publicationDate != null) {
            return publicationDate.getValue();
        } else {
            return 0;
        }
    }*/

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

    public List<String> getRelatedIDs() {

        if (relatedUrls != null) {
            return relatedUrls;
        } else {
            return new ArrayList<>();
        }
    }

    public Map<Integer, Set<Review>> getMappedReviews() {

        return mappedReviews;
    }

    public Map<Integer, OpinionValue> getMappedOpinions() {

        return mappedOpinions;
    }
}
