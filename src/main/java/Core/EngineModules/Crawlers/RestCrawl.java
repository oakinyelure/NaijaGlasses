package Core.EngineModules.Crawlers;

import DataContext.Exceptions.MongoEntityException;
import DataContext.Models.DomLink;
import DataContext.Mongo;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RestCrawl {
    private HashSet<URI> _intranetDocuments;

    private HashMap _crawlResults;

    private HttpClient _http;

    public RestCrawl() throws URISyntaxException, MongoEntityException {
        this._http = HttpClient.newHttpClient();
        this._crawlResults = new HashMap();
        this._intranetDocuments = new HashSet<URI>();
        this._setDocumentsToCrawl();
    }

    public RestCrawl initAsync() {
        List<CompletableFuture<String>> requests = this._intranetDocuments
                .stream()
                .map(url -> this._http.sendAsync(
                        HttpRequest.newBuilder(url)
                                .header("Accept","text/html")
                        .GET()
                        .build(), HttpResponse.BodyHandlers.ofString())
                        .thenApply(response -> {
                            if(response.statusCode() == 200) {
                                _crawlResults.put(url.toString(), response.body());
                            }
                            return response.body();
                        })
                        .thenApply(path -> path.toString()))
                .collect(Collectors.toList());

        CompletableFuture.allOf(requests.toArray(new CompletableFuture<?>[0])).join();

        return this;
    }

    public Map getCrawlResults() {
        return this._crawlResults;
    }

    private void _setDocumentsToCrawl() throws MongoEntityException, URISyntaxException {
        Mongo mongo = new Mongo("naija_glasses_db");
        Datastore orm = mongo.getOrmInstance();
        List<DomLink> storedLinks = orm.createQuery(DomLink.class).asList(new FindOptions().limit(1000));
        for(DomLink link : storedLinks) {
            this._intranetDocuments.add(new URI(link.Link));
        }
    }
}
