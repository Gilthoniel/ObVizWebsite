import constants.Constants;
import models.WebPage;
import models.errors.NoAppFoundException;
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

        final WebPage webpage = new WebPage(session());

        if (exception.getMessage() == null) {

            return internalServerError(exception, webpage);
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

                return internalServerError(exception, webpage);
        }
    }

    private F.Promise<Result> internalServerError(Throwable exception, final WebPage webpage) {
        writeLogs(exception);

        String message = "Internal server error. Please contact an administrator if it persists";
        return F.Promise.pure(
                Results.internalServerError((play.twirl.api.Html) views.html.error.render(webpage, message))
        );
    }

    private void writeLogs(Throwable error) {
        // Write the message
        Logger.error("Error page occurred with message : " + error.getClass() + " - " + error.getMessage() + "\n");

        // Write the cause
        if (error.getCause() != null) {
            Logger.error(error.getCause().getMessage());
        }

        // Write the lines
        StringBuilder traces = new StringBuilder();
        int max = Math.min(error.getStackTrace().length, 5);
        for (int i = 0; i < max; i++) {
            traces.append(" -> ").append(error.getStackTrace()[i]).append("\n");
        }
        Logger.error(traces.toString());
    }
}
