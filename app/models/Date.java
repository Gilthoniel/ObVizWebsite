package models;

import java.text.SimpleDateFormat;

/**
 * Created by gaylor on 29.06.15.
 * Mango DB Date representation
 */
public class Date {

    private long $date;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM YYYY");

    public long getValue() {
        return $date;
    }

    public String toString() {

        return formatter.format(new java.util.Date($date));
    }
}
