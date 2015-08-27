package models;

import controllers.Login;
import controllers.routes;
import play.Play;
import play.mvc.Call;
import play.mvc.Http;
import service.BaseUser;
import service.BaseUserService;
import service.CategoryManager;
import service.TopicsManager;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gaylor on 03.07.15.
 * Basic information about a web page
 */
public class WebPage {

    private List<WebPath> breadcrumb;
    private BaseUser user;
    private TopicsManager mTopics;
    private CategoryManager mCategories;

    public WebPage(TopicsManager topics, CategoryManager categories, Http.Session session) {
        mTopics = topics;
        mCategories = categories;

        user = Login.getLocalUser(session);

        breadcrumb = new LinkedList<>();
        addPath(routes.Application.index(), "Home");
    }

    /**
     * Add an entry for the breadcrumb on the top of the page
     * @param path url
     * @param title name
     */
    public void addPath(Call path, String title) {
        breadcrumb.add(new WebPath(path, title));
    }

    public void addAllPaths(Collection<WebPath> collection) {

        breadcrumb.clear();
        breadcrumb.addAll(collection);
    }

    /**
     * Get the breadcrumb
     * @return list of paths
     */
    public List<WebPath> getBreadcrumb() {
        return breadcrumb;
    }

    public BaseUser getUser() {
        return user;
    }

    public TopicsManager getTopics() {
        return mTopics;
    }

    public CategoryManager getCategories() {
        return mCategories;
    }

    public boolean isProdMode() {

        return Play.isProd();
    }

    public boolean isAdmin() {

        return user != null && user.right == BaseUserService.Rights.ADMIN;
    }

    /* PRIVATE */

    /**
     * Represent a path with a title for the breadcrumb in the top of the page
     */
    public static class WebPath {
        private Call path;
        private String title;
        private boolean isActive;

        public WebPath(Call path, String title) {
            this.path = path;
            this.title = title;
            isActive = false;
        }

        public WebPath(Call call, String title, boolean active) {
            this(call, title);
            isActive = active;
        }

        public Call getPath() {
            return path;
        }

        public String getTitle() {
            return title;
        }

        public boolean isActive() {
            return isActive;
        }

        public void activate() {
            isActive = true;
        }
    }
}
