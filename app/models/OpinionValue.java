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
    private double generalOpinionValue;

    public int percentage() {

        return (int) Math.floor(generalOpinionValue * 100);
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
