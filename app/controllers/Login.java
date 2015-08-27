package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import models.WebPage;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import service.BaseUser;

/**
 * Created by gaylor on 16.07.15.
 * Controller for Play Authenticate pages
 */
@With(WebPageInformation.class)
public class Login extends Controller {

    public Result login() {
        WebPage webpage = (WebPage) Http.Context.current().args.get("com.obviz.webpage");

        return ok((play.twirl.api.Html) views.html.login.login.render(webpage));
    }

    public Result oAuthDenied(String provider) {

        return ok();
    }

    public static BaseUser getLocalUser(final Http.Session session) {

        return BaseUser.findByAuthUserIdentity(PlayAuthenticate.getUser(session));
    }
}
