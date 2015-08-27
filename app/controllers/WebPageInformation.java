package controllers;

import com.google.inject.Inject;
import models.WebPage;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import service.CategoryManager;
import service.TopicsManager;

/**
 * Created by gaylor on 08/26/2015.
 * Webpage important parameters
 */
public class WebPageInformation extends Action.Simple {

    private TopicsManager topics;
    private CategoryManager categories;

    @Inject
    public WebPageInformation(TopicsManager topics, CategoryManager categories) {
        this.topics = topics;
        this.categories = categories;
    }

    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {

        context.args.put("com.obviz.webpage", new WebPage(topics, categories, context.session()));

        return delegate.call(context);
    }
}
