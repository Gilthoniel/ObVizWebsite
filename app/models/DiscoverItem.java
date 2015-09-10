package models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaylor on 09/09/2015.
 * Subgroup of discover searching
 */
public class DiscoverItem implements Serializable {
    private static final long serialVersionUID = 482494166221568803L;

    public String title;
    public List<AndroidApp> apps;
    public Integer topicID;
    public String category;
}
