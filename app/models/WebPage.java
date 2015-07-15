package models;

import controllers.routes;
import play.mvc.Call;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gaylor on 03.07.15.
 * Basic information about a web page
 */
public class WebPage {

    private List<WebPath> breadcrumb;

    public WebPage() {
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

    /**
     * Get the breadcrumb
     * @return list of paths
     */
    public List<WebPath> getBreadcrumb() {
        return breadcrumb;
    }

    /* PRIVATE */

    /**
     * Represent a path with a title for the breadcrumb in the top of the page
     */
    public class WebPath {
        private Call path;
        private String title;

        public WebPath(Call path, String title) {
            this.path = path;
            this.title = title;
        }

        public Call getPath() {
            return path;
        }

        public String getTitle() {
            return title;
        }
    }
}
