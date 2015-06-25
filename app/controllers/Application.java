package controllers;

import play.Play;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    /**
     * Homepage
     * @return html result
     */
    public Result index() {

        return ok(views.html.index.render());
    }

    /**
     * Application page where we find information about it
     * @return html result
     */
    public Result details(String id) {

        return ok(views.html.details.render(id));
    }

}
