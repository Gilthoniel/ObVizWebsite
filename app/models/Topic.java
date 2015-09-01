package models;

import play.data.DynamicForm;
import webservice.MessageParser;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by gaylor on 02.07.15.
 * Titles of a topic related to its ID
 */
public class Topic implements Serializable {

    private static final long serialVersionUID = -7874128752238578952L;
    private int _id;
    private String title;
    private String type;
    private String category;
    private String appID;
    private String name;
    private List<String> categories;
    private String[] keys;
    private boolean useSpecialGauge;
    private double gaugeThreshold;

    public Topic(DynamicForm form) {
        _id = MessageParser.parseInt(form.get("id"));
        title = form.get("title");
        type = form.get("type");
        name = form.get("name");
        gaugeThreshold = Double.parseDouble(form.get("threshold"));

        String tempKeys = form.get("keys");
        if (tempKeys != null && !tempKeys.isEmpty()) {
            keys = tempKeys.split("\\s*,\\s*");
        } else {
            keys = new String[]{};
        }

        categories = new LinkedList<>();
        for (Map.Entry<String, String> entry : form.data().entrySet()) {
            if (entry.getKey().startsWith("categories")) {
                categories.add(entry.getValue());
            }
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

    public double getGaugeThreshold() {
        return gaugeThreshold;
    }

    public boolean isSpecial() {
        return useSpecialGauge;
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

    public String getName() {
        return name != null ? name : "";
    }

    public List<String> getCategories() {
        return categories != null ? categories : Collections.EMPTY_LIST;
    }

    public String[] getKeys() {
        return keys;
    }
}
