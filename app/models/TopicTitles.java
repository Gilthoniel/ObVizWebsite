package models;

import play.data.DynamicForm;
import webservice.MessageParser;

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
    private String type;
    private String category;
    private String appID;
    private String[] keys;

    public TopicTitles(DynamicForm form) {
        _id = MessageParser.parseInt(form.get("id"));
        title = form.get("title");
        type = form.get("type");

        String tempKeys = form.get("keys");
        if (tempKeys != null && !tempKeys.isEmpty()) {
            keys = tempKeys.split("\\s*,\\s*");
        } else {
            keys = new String[]{};
        }

        if (form.data().containsKey("category")) {
            category = form.get("category");
        }

        if (form.data().containsKey("app")) {
            appID = form.get("app");
        }
    }

    public boolean isValid() {

        return _id > 0 && title != null && !title.isEmpty() && type != null && !type.isEmpty();
    }

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

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category != null ? category : "";
    }

    public String getAppID() {
        return appID != null ? appID : "";
    }

    public String[] getKeys() {
        return keys;
    }
}
