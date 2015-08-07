package models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by gaylor on 29.06.15.
 * Mango DB Date representation
 */
public class Date implements Serializable {

    private static final long serialVersionUID = -1812320280233313292L;
    private long $date;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM YYYY");

    public long getValue() {
        return $date;
    }

    public String getWithMinutes() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss, dd MMMM YYYY");

        return formatter.format(new java.util.Date($date));
    }

    public String toString() {

        return formatter.format(new java.util.Date($date));
    }
}
