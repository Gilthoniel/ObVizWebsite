package models;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.Serializable;

/**
 * Created by gaylor on 02.07.15.
 * Separate positive and negative opinions
 */
public class OpinionValue implements Comparable<OpinionValue>, Serializable {

    private static final long serialVersionUID = 5457078665521570453L;
    private int numberPositive;
    private int numberNegative;
    private int topicID;

    public OpinionValue(int id) {
        topicID = id;
    }

    public void addPositives(int value) {
        numberPositive += value;
    }

    public void setPositive(int value) {
        numberPositive = value;
    }

    public void addNegatives(int value) {
        numberNegative += value;
    }

    public void setNegative(int value) {
        numberNegative = value;
    }

    public int percentage() {
        int percent = Math.round(numberPositive * 100 / (numberNegative + numberPositive));

        if (percent == 0) {
            return 1;
        } else {
            return percent;
        }
    }

    public int getTotal() {
        return numberNegative + numberPositive;
    }

    public int getNumberPositive() {
        return numberPositive;
    }

    public int getNumberNegative() {
        return numberNegative;
    }

    public int getTopicID() {
         return topicID;
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

        return Integer.compare(other.getTotal(), numberNegative + numberPositive);
    }
}
