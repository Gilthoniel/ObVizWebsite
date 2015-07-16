package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by gaylor on 16.07.15.
 *
 */
public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(final Http.Context ctx) {
        final AuthUser user = PlayAuthenticate.getUser(ctx.session());

        if (user != null) {
            return user.getId();
        } else {
            return null;
        }
    }

    @Override
    public Result onUnauthorized(final Http.Context ctx) {

        return redirect(routes.Login.login());
    }
}
