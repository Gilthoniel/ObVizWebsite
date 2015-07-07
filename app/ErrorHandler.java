import constants.Constants;
import models.WebPage;
import play.Logger;
import play.http.HttpErrorHandler;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

/**
 * Created by gaylor on 07.07.15.
 * Dispatch errors
 */
public class ErrorHandler implements HttpErrorHandler {
    @Override
    public F.Promise<Result> onClientError(Http.RequestHeader request, int statusCode, String message) {
        return F.Promise.<Result>pure(
                Results.status(statusCode, "A client error occurred: " + message)
        );
    }

    @Override
    public F.Promise<Result> onServerError(Http.RequestHeader request, Throwable exception) {

        Logger.error("Error page occurred with message : " + exception.getMessage());

        final WebPage webpage = new WebPage();

        String message;
        switch (exception.getMessage()) {
            case Constants.NO_APP_EXCEPTION:
                message = "Sorry, we can't find this app. Did you use the search bar ?";
                break;
            case Constants.SERVER_OVERLOADED_EXCEPTION:
                message = "Server is currently overloaded. Please come back later.";
                break;
            default:
                message = "Internal server error. Please contact an administrator if it persists";
                break;
        }

        return F.Promise.pure(
                Results.ok((play.twirl.api.Html) views.html.error.render(webpage, message))
        );
    }
}
