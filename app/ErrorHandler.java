import com.google.inject.Inject;
import constants.Constants;
import models.WebPage;
import play.Configuration;
import play.Environment;
import play.Logger;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Provider;

/**
 * Created by gaylor on 07.07.15.
 * Dispatch errors
 */
public class ErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public ErrorHandler(Configuration configuration, Environment environment, OptionalSourceMapper mapper,
                        Provider<Router> router)
    {
        super(configuration, environment, mapper, router);
    }

    @Override
    protected void logServerError(Http.RequestHeader request, UsefulException exception) {

        Logger.error("Request terminated with errors : "+request.path());

        writeLogs(exception);
    }

    @Override
    public F.Promise<Result> onClientError(Http.RequestHeader request, int statusCode, String message) {
        final WebPage webpage = new WebPage();

        return F.Promise.<Result>pure(
                Results.status(statusCode, (play.twirl.api.Html) views.html.error.render(webpage, message))
        );
    }

    @Override
    protected F.Promise<Result> onProdServerError(Http.RequestHeader request, UsefulException exception) {

        final WebPage webpage = new WebPage();
        String message = "It seems that the server is overloaded. Please come back later :-)";

        return F.Promise.pure(
                Results.badRequest((play.twirl.api.Html) views.html.error.render(webpage, message))
        );
    }

    private void writeLogs(UsefulException error) {
        // Write the message
        Logger.error("Error page occurred with message : " + error.title + "\n\n" +
                error.description + "\n");

        // Write the lines
        StringBuilder traces = new StringBuilder();
        int max = Math.min(error.cause.getStackTrace().length, 10);
        for (int i = 0; i < max; i++) {
            traces.append(" -> ").append(error.cause.getStackTrace()[i]).append("\n");
        }
        Logger.error(traces.toString());
    }
}
