package models;

import java.util.List;

/**
 * Created by gaylor on 02.07.15.
 * Titles of a topic related to its ID
 */
public class TopicTitles {

    private int _id;
    private List<String> keys;

    public int getID() {
        return _id;
    }

    public List<String> getTitles() {
        return keys;
    }
}
