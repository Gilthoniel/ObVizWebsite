package models;

import play.Logger;

/**
 * Created by gaylor on 02.07.15.
 * Separate positive and negative opinions
 */
public class OpinionValue {

    private int numberPositive;
    private int numberNegative;

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

    /**
     * Get the relative font size in em
     * @param max the maximum number of opinions for the entire topics
     * @return
     */
    public int getFontSize(int max) {
        return Math.round(1 + 2 * getTotal() / (float) max);
    }
}
