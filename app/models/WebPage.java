package models;

import controllers.Login;
import controllers.routes;
import play.Play;
import play.mvc.Call;
import play.mvc.Http;
import service.BaseUser;
import service.CategoryManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gaylor on 03.07.15.
 * Basic information about a web page
 */
public class WebPage {

    private List<WebPath> breadcrumb;
    private BaseUser user;

    public WebPage(Http.Session session) {
        breadcrumb = new LinkedList<>();
        addPath(routes.Application.index(), "Home");

        user = Login.getLocalUser(session);
    }

    public WebPage(Http.Session session, List<WebPath> paths) {
        user = Login.getLocalUser(session);
        breadcrumb = paths;
    }

    /**
     * Add an entry for the breadcrumb on the top of the page
     * @param path url
     * @param title name
     */
    public void addPath(Call path, String title) {
        breadcrumb.add(new WebPath(path, title));
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

    public List<CategorySet> getCategories() {
        return CategoryManager.instance.getCategories();
    }

    public boolean isProdMode() {

        return Play.isProd();
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
