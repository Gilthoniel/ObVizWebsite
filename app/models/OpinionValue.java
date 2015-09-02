package models;

import edu.umd.cs.findbugs.annotations.NonNull;
import play.Logger;
import service.TopicsManager;

import java.io.Serializable;

/**
 * Created by gaylor on 02.07.15.
 * Separate positive and negative opinions
 */
public class OpinionValue implements Comparable<OpinionValue>, Serializable {

    private static final long serialVersionUID = 5457078665521570453L;
    private int nbPositiveOpinions;
    private int nbNegativeOpinions;
    private int topicID;
    private int percent = -1;

    public void compute(TopicsManager manager, int nbReviews) {

        Topic topic = manager.getTopic(topicID);
        if (topic != null && topic.isSpecial()) {

            double value = (nbReviews - (nbNegativeOpinions - nbPositiveOpinions) / topic.getGaugeThreshold()) / nbReviews;
            value *= 100;
            percent = (int) Math.max(0, Math.min(100, value));

        }
    }

    public int percentage() {

        if (percent < 0) {
            if (nbPositiveOpinions <= 0 && nbNegativeOpinions <= 0) {
                percent = 0;
            }

            percent = Math.round(nbPositiveOpinions * 100 / (nbNegativeOpinions + nbPositiveOpinions));

            if (percent == 0) {
                percent = 1;
            }
        }

        return percent;
    }

    public int getTotal() {
        return nbNegativeOpinions + nbPositiveOpinions;
    }

    public int getNumberPositive() {
        return nbPositiveOpinions;
    }

    public int getNumberNegative() {
        return nbNegativeOpinions;
    }

    public int getTopicID() {
         return topicID;
    }

    public boolean isValid() {

        return topicID > 0 && (nbNegativeOpinions > 0 || nbPositiveOpinions > 0);
    }

    /**
     * Get the relative font size in em
     * @param max the maximum number of opinions for the entire topics
     * @return size of the font
     */
    public float getFontSize(int max) {
        return 1 + getTotal() / (float) max;
    }

    @Override
    public int compareTo(@NonNull OpinionValue other) {

        return Integer.compare(other.getTotal(), nbNegativeOpinions + nbPositiveOpinions);
    }
}
