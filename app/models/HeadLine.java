package models;

import java.util.List;

/**
 * Created by Gaylor on 17.09.2015.
 * HeadLine in the Home page
 */
public class HeadLine {

    private String title;
    private Integer topicID;
    private List<AndroidApp> apps;

    public String getTitle() {
        return title;
    }

    public Integer getTopicID() {
        return topicID;
    }

    public List<AndroidApp> getApps() {
        return apps;
    }
}
