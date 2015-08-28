package service;

import com.google.inject.Inject;
import constants.Constants;
import models.Category;
import models.CategoryType;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import play.libs.F;
import service.cache.CustomCache;
import webservice.WebService;

import javax.inject.Singleton;
import java.util.*;

/**
 * Created by gaylor on 06-Aug-15.
 * List of set of categories
 */
@Singleton
public class CategoryManager {

    public static final String CACHE_KEY = "com.obviz.category";

    private WebService wb;
    private Cache mCache;

    @Inject
    public CategoryManager(WebService webservice, CustomCache cache) {
        wb = webservice;
        mCache = cache.getPinnedCache();
    }

    public List<Wrapper> getSuperCategories() {

        Container container = getContainer();

        List<Wrapper> list = new LinkedList<>();

        for (Map.Entry<Integer, List<Category>> entry : container.mTypes.entrySet()) {
            CategoryType type = container.mTypeTitles.get(entry.getKey());

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

    public Category getFrom(String category, boolean firstTry) {

        Container container = getContainer();

        if (container.mCategories.containsKey(category)) {

            return container.mCategories.get(category);
        } else if (firstTry) {

            mCache.remove(CACHE_KEY);
            return getFrom(category, false);

        } else {

            return Category.instanceDefault;
        }
    }

    public Category getFrom(String category) {

        return getFrom(category, true);
    }

    private synchronized Container init() {

        if (mCache.isElementInMemory(CACHE_KEY)) {

            return (Container) mCache.get(CACHE_KEY).getObjectValue();
        }

        final Container container = new Container();

        F.Promise<List<CategoryType>> promiseTypes = wb.getCategoryTypes();
        F.Promise<List<Category>> promiseCategories = wb.getCategories();

        return promiseTypes.flatMap(types -> {

            container.mTypes = new HashMap<>();
            container.mTypeTitles = new HashMap<>();
            for (CategoryType type : types) {
                container.mTypes.put(type._id, new LinkedList<>());
                container.mTypeTitles.put(type._id, type);
            }

            return promiseCategories.map(categories -> {

                container.mCategories = new HashMap<>();
                for (Category cat : categories) {
                    container.mCategories.put(cat.category, cat);

                    for (int type : cat.types) {
                        container.mTypes.get(type).add(cat);
                    }
                }

                mCache.put(new Element(CACHE_KEY, container));
                return container;
            });
        }).get(Constants.TIMEOUT);
    }

    private Container getContainer() {

        Element element = mCache.get(CACHE_KEY);
        if (element != null) {
            return (Container) element.getObjectValue();
        } else {

            return init();
        }
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
