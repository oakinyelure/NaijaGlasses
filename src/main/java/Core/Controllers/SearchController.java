package Core.Controllers;

import Core.EngineModules.Crawlers.RestCrawl;

public class SearchController extends ApiController {
    public SearchController() {}

    public Object Search() {
        try {
            RestCrawl crawledContent = new RestCrawl();
            crawledContent.initAsync();
            return crawledContent.getCrawlResults();
        }
        catch (Exception ex) {
            response.status(500);
            return ex;
        }
    }
}
