package Core.EngineModules.Dom;

import Core.EngineModules.Crawlers.RestCrawl;

import java.net.URISyntaxException;
import java.util.Map;

public class DomParser {

    private boolean _crawlerActive;

    private boolean _saveMode;

    private RestCrawl _crawler;

    public DomParser() throws URISyntaxException {
        this._crawler = new RestCrawl();
    }

    public void parseAsync() {
        this._crawler.initAsync();
        Map<String,String> resultSet = this._crawler.getCrawlResults();
    }

}
