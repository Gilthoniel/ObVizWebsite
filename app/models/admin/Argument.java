package models.admin;

import models.Date;
import models.ID;

import java.util.List;

/**
 * Created by gaylor on 29.07.15.
 * Argument in a review
 */
public class Argument {

    private ID _id;
    private String appID;
    private String permalink;
    private String side;
    private int type;
    private String text;
    private Date createdAt;
    private ID reviewID;
    private List<Component> components;

    public String getAppID() {
        return appID;
    }
}
