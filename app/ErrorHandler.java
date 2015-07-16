import constants.Constants;
import models.WebPage;
import play.Logger;
import play.http.HttpErrorHandler;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.Arrays;

/**
 * Created by gaylor on 07.07.15.
 * Dispatch errors
 */
public class ErrorHandler extends Controller implements HttpErrorHandler {
    @Override
    public F.Promise<Result> onClientError(Http.RequestHeader request, int statusCode, String message) {
        final WebPage webpage = new WebPage(session());

        return F.Promise.<Result>pure(
                Results.status(statusCode, (play.twirl.api.Html) views.html.error.render(webpage, message))
        );
    }

    @Override
    public F.Promise<Result> onServerError(Http.RequestHeader request, Throwable exception) {

        Logger.error("Error page occurred with message : " + exception.getMessage());

        final WebPage webpage = new WebPage(session());

        if (exception.getMessage() == null) {
            for (StackTraceElement trace : exception.getStackTrace()) {
                Logger.error(trace.toString());
            }

            String message = "Server is currently overloaded. Please come back later.";
            return F.Promise.pure(
                    Results.internalServerError((play.twirl.api.Html) views.html.error.render(webpage, message))
            );
        }

        String message;
        switch (exception.getMessage()) {
            case Constants.NO_APP_EXCEPTION:
                message = "Sorry, we can't find this app. Did you use the search bar ?";
                return F.Promise.pure(
                        Results.notFound((play.twirl.api.Html) views.html.error.render(webpage, message))
                );
            case Constants.SERVER_OVERLOADED_EXCEPTION:
                message = "Server is currently overloaded. Please come back later.";
                return F.Promise.pure(
                        Results.internalServerError((play.twirl.api.Html) views.html.error.render(webpage, message))
                );
            default:
                message = "Internal server error. Please contact an administrator if it persists";
                return F.Promise.pure(
                        Results.internalServerError((play.twirl.api.Html) views.html.error.render(webpage, message))
                );
        }
    }
}
