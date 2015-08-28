package models;

import play.data.DynamicForm;
import webservice.MessageParser;

/**
 * Created by gaylor on 08/26/2015.
 * Set of different categories
 */
public class CategoryType {

    public Integer _id;
    public String title;
    public String icon;
    public boolean active;

    public CategoryType(DynamicForm form) {
        int id = MessageParser.parseInt(form.get("id"));
        if (id > 0) {
            _id = id;
        }
        title = form.get("title");
        icon = form.get("icon");
        active = form.get("active") != null;
    }

    public boolean isValid() {

        return title != null && !title.isEmpty();
    }

    public String getIcon() {
        return icon != null && !icon.isEmpty() ? icon : "default.png";
    }

    public boolean isActive() {
        return active;
    }
}
