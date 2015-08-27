package service;

import com.google.inject.Inject;
import constants.Constants;
import models.Category;
import models.CategoryType;
import play.libs.F;
import webservice.WebService;

import javax.inject.Singleton;
import java.util.*;

/**
 * Created by gaylor on 06-Aug-15.
 * List of set of categories
 */
@Singleton
public class CategoryManager {

    @Inject
    private WebService wb;

    private Map<String, Category> mCategories;
    private Map<Integer, List<Category>> mTypes;
    private Map<Integer, String> mTypeTitles;
    private boolean isInitialize = false;

    public List<Wrapper> getSuperCategories() {

        if (!isInitialize) {
            init();
        }

        List<Wrapper> list = new LinkedList<>();

        for (Map.Entry<Integer, List<Category>> entry : mTypes.entrySet()) {
            Wrapper wrapper = new Wrapper();
            wrapper.title = mTypeTitles.get(entry.getKey());

            StringJoiner joiner = new StringJoiner(",");
            for (Category category : entry.getValue()) {
                joiner.add(category.category);
            }
            wrapper.categories = String.join(",", joiner.toString());

            list.add(wrapper);
        }

        return list;
    }

    public Category getFrom(String category) {

        if (!isInitialize) {
            init();
        }

        if (mCategories.containsKey(category)) {

            return mCategories.get(category);
        } else {

            return Category.instanceDefault;
        }
    }

    private synchronized void init() {

        if (isInitialize) {
            return;
        }

        F.Promise<List<CategoryType>> promiseTypes = wb.getCategoryTypes();
        F.Promise<List<Category>> promiseCategories = wb.getCategories();

        promiseTypes.flatMap(types -> {

            mTypes = new HashMap<>();
            mTypeTitles = new HashMap<>();
            for (CategoryType type : types) {
                mTypes.put(type._id, new LinkedList<>());
                mTypeTitles.put(type._id, type.title);
            }

            return promiseCategories.map(categories -> {

                mCategories = new HashMap<>();
                for (Category cat : categories) {
                    mCategories.put(cat.category, cat);

                    for (int type : cat.types) {
                        mTypes.get(type).add(cat);
                    }
                }

                isInitialize = true;

                return null;
            });
        }).get(Constants.TIMEOUT);
    }

    public class Wrapper {

        public String title;
        public String categories;
    }
}
