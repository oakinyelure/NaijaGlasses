package Core.Controllers;


import Core.EngineModules.Search.SearchEngine;
import Core.Exceptions.InvalidSearchRequest;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class SearchController extends ApiController {
    public SearchController() {}

    public Object Search() {
        try {
            String searchKeyword = Jsoup.clean(this.request.queryParams("q"), Whitelist.simpleText());
            SearchEngine search = new SearchEngine();
            return search.get(searchKeyword);
        }
        catch (Exception ex) {
            response.status(500);
            return ex;
        }
    }
}
