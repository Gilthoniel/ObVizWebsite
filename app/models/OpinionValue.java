package models;

import edu.umd.cs.findbugs.annotations.NonNull;

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

    public OpinionValue(int id) {
        topicID = id;
    }

    public int percentage() {
        if (nbPositiveOpinions <= 0 && nbNegativeOpinions <= 0) {
            return 0;
        }

        int percent = Math.round(nbPositiveOpinions * 100 / (nbNegativeOpinions + nbPositiveOpinions));

        if (percent == 0) {
            return 1;
        } else {
            return percent;
        }
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

        return topicID > 0 && nbNegativeOpinions > 0 && nbPositiveOpinions > 0;
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
