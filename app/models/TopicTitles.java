package models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaylor on 02.07.15.
 * Titles of a topic related to its ID
 */
public class TopicTitles implements Serializable {

    private static final long serialVersionUID = -7874128752238578952L;
    private int _id;
    private String title;
    private String[] keys;

    public int getID() {
        return _id;
    }

    public String getTitle() {

        if (title != null) {
            return title;
        } else if (keys != null && keys.length > 0) {

            return keys[0];
        } else {
            return "Unknown";
        }
    }
}
