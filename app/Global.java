import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;
import controllers.routes;
import play.Application;
import play.GlobalSettings;
import play.mvc.Call;

/**
 * Created by gaylor on 16.07.15.
 * Global settings
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(final Application app) {

        /** Routes for Play Authenticate **/
        PlayAuthenticate.setResolver(new PlayAuthenticate.Resolver() {
            @Override
            public Call login() {

                return controllers.routes.Login.login();
            }

            @Override
            public Call afterAuth() {

                return routes.Administration.admin();
            }

            @Override
            public Call afterLogout() {
                return routes.Application.index();
            }

            @Override
            public Call auth(final String provider) {

                return com.feth.play.module.pa.controllers.routes.Authenticate.authenticate(provider);
            }

            @Override
            public Call onException(final AuthException e) {

                if (e instanceof AccessDeniedException) {
                    return routes.Login.oAuthDenied(((AccessDeniedException) e).getProviderKey());
                }

                return super.onException(e);
            }

            @Override
            public Call askLink() {

                return null;
            }

            @Override
            public Call askMerge() {

                return null;
            }
        });
    }
}
