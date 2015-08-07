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
    private List<String> keys;

    public int getID() {
        return _id;
    }

    public List<String> getTitles() {
        return keys;
    }
}
