package Core.Controllers;


import Core.EngineModules.Search.SearchEngine;

public class SearchController extends ApiController {
    public SearchController() {}

    public Object Search() {
        try {
            SearchEngine search = new SearchEngine();
            return search.get("test");
        }
        catch (Exception ex) {
            response.status(500);
            return ex;
        }
    }
}
