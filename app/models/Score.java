package models;

import java.io.Serializable;

/**
 * Created by gaylor on 01.07.15.
 * Score of an application
 */
public class Score implements Serializable {

    private static final long serialVersionUID = 5559762968571332410L;
    private double total;
    private int count;
    private int one;
    private int two;
    private int three;
    private int four;
    private int five;

    public double getTotal() {
        return total;
    }
}
