package models;

import constants.Constants.Category;
import play.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by gaylor on 20.07.15.
 * A set of categories represented by a title and a description
 */
public class CategorySet implements Serializable {

    private static final long serialVersionUID = 2861394878954222073L;
    private String title;
    private Category[] categories;

    public CategorySet(String title) {
        this.title = title;
        categories = new Category[] {};
    }

    public void addAll(Category[] categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder();

        // Take a maximum of 5 elements
        for (Category category : Arrays.copyOf(categories, categories.length > 5 ? 5 : categories.length)) {
            builder.append(category.getTitle()).append(", ");
        }

        if (categories.length > 5) {
            builder.append("etc...");
        } else {
            builder.delete(builder.length() - 2, builder.length());
        }

        return builder.toString();
    }

    public String getCategories() {
        StringBuilder builder = new StringBuilder();

        Iterator<Category> it = Arrays.asList(categories).iterator();
        while (it.hasNext()) {
            Category category = it.next();

            builder.append(category.name());
            if (it.hasNext()) {
                builder.append(",");
            }
        }

        return builder.toString();
    }
}
