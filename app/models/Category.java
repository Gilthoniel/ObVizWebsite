package models;

import constants.Utils;
import play.Logger;
import play.data.DynamicForm;
import webservice.MessageParser;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by gaylor on 08/26/2015.
 * Category of the Google Play Store
 */
public class Category {

    public static final Category instanceDefault = new Category();
    static {
        instanceDefault.category = "DEFAULT";
        instanceDefault.title = "Application";
        instanceDefault.types = new LinkedList<>();
    }

    public ID _id;
    public String category;
    public String title;
    public List<Integer> types;
    public String icon;

    private Category() {}

    public Category(DynamicForm form) {
        _id = ID.create(form.get("id"));
        category = form.get("category");
        title = form.get("title");

        types = new LinkedList<>();
        for (Map.Entry<String,String> entry : form.data().entrySet()) {

            if (entry.getKey().startsWith("types")) {
                types.add(Utils.parseInt(entry.getValue()));
            }
        }

        icon = form.get("icon") != null ? form.get("icon") : "";
    }

    public boolean isValid() {

        return category != null && !category.isEmpty() && title != null && !title.isEmpty();
    }
}
