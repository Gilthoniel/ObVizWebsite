package models;

import constants.Constants.Category;
import play.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by gaylor on 20.07.15.
 * A set of categories represented by a title and a description
 */
public class CategorySet {

    private String title;
    private Category[] categories;
    private String description;

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

        for (Category category : categories) {
            builder.append(category.toString()).append(",");
        }
        builder.delete(builder.length() - 1, builder.length());

        return builder.toString();
    }
}
