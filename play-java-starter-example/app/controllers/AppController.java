package controllers;

import play.mvc.*;
import services.TMDbService;
import views.html.*;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class AppController extends Controller {

    TMDbService tmdbService;

    @Inject
    public AppController(TMDbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    public CompletionStage<Result> search(String query, String category, Http.Request request) {
        return tmdbService.search(query, category)
                .thenApply(searchResults -> {
                    return ok(views.html.searchResults.render((tmdbService.getAllSearches(searchResults))))
                            .addingToSession(request, "queryList", tmdbService.getJsonQueryList());
                });
    }

    public Result index() {
        return ok(
                home.render());
    }

}
