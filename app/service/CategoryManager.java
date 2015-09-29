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

    private WebService wb;
    private Container mContainer;
    private final Object mLock = new Object();

    @Inject
    public CategoryManager(WebService webservice) {
        wb = webservice;

        init();
    }

    public List<Wrapper> getSuperCategories() {

        synchronized (mLock) {

            while (mContainer == null) {
                try {
                    mLock.wait();
                } catch (InterruptedException ignored) {}
            }

            List<Wrapper> list = new LinkedList<>();

            for (Map.Entry<Integer, List<Category>> entry : mContainer.mTypes.entrySet()) {
                CategoryType type = mContainer.mTypeTitles.get(entry.getKey());

                if (type.isActive()) {
                    Wrapper wrapper = new Wrapper();
                    wrapper.categoryType = type;

                    StringJoiner joiner = new StringJoiner(",");
                    for (Category category : entry.getValue()) {
                        joiner.add(category.category);
                    }
                    wrapper.categories = String.join(",", joiner.toString());

                    list.add(wrapper);
                }
            }

            return list;
        }
    }

    public Category getFrom(String category, boolean firstTry) {

        synchronized (mLock) {

            while (mContainer == null) {
                try {
                    mLock.wait();
                } catch (InterruptedException ignored) {
                }
            }

            if (mContainer.mCategories.containsKey(category)) {

                return mContainer.mCategories.get(category);
            } else if (firstTry) {

                init();
                return getFrom(category, false);

            } else {

                return Category.instanceDefault;
            }
        }
    }

    public Category getFrom(String category) {

        return getFrom(category, true);
    }

    public void init() {

        F.Promise<List<CategoryType>> promiseTypes = wb.getCategoryTypes();
        F.Promise<List<Category>> promiseCategories = wb.getCategories();

        promiseTypes.flatMap(types -> promiseCategories.map(categories -> {

            synchronized (mLock) {

                mContainer = new Container();

                mContainer.mTypes = new HashMap<>();
                mContainer.mTypeTitles = new HashMap<>();
                for (CategoryType type : types) {
                    mContainer.mTypes.put(type._id, new LinkedList<>());
                    mContainer.mTypeTitles.put(type._id, type);
                }

                mContainer.mCategories = new HashMap<>();
                for (Category cat : categories) {
                    mContainer.mCategories.put(cat.category, cat);

                    for (int type : cat.types) {
                        mContainer.mTypes.get(type).add(cat);
                    }
                }

                mLock.notifyAll();
            }

            return null;
        })).get(Constants.TIMEOUT);
    }

    public class Wrapper {

        public CategoryType categoryType;
        public String categories;
    }

    private class Container {
        private Map<String, Category> mCategories;
        private Map<Integer, List<Category>> mTypes;
        private Map<Integer, CategoryType> mTypeTitles;
    }
}
