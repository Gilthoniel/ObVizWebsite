package controllers;

import models.WebPage;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import service.BaseUserService;

/**
 * Created by gaylor on 08/27/2015.
 * Admin simple action
 */
public class WebPageAdmin extends Action.Simple {

    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {

        WebPage webpage = new WebPage(null, null, context.session());
        context.args.put("com.obviz.webpage", webpage);
        if (webpage.getUser() == null || webpage.getUser().right != BaseUserService.Rights.ADMIN) {

            return F.Promise.pure(redirect(routes.Application.index()));
        } else {

            return delegate.call(context);
        }
    }
}
