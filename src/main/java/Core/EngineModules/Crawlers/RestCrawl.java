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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RestCrawl {
    private HashSet<URI> _intranetDocuments;

    private HashMap<String, String> _crawlResults;

    private HttpClient _http;

    public RestCrawl() throws URISyntaxException, MongoEntityException {
        this._http = HttpClient.newHttpClient();
        this._crawlResults = new HashMap<>();
        this._intranetDocuments = new HashSet<>();
        this._setDocumentsToCrawl();
    }

    public RestCrawl initAsync() {
        List<CompletableFuture<String>> requests = this._intranetDocuments
                .stream()
                .map(url ->
                    this._http.sendAsync(
                            HttpRequest.newBuilder(url)
                                    .header("Accept", "text/html")
                                    .GET()
                                    .build(), HttpResponse.BodyHandlers.ofString())
                            .orTimeout(20, TimeUnit.SECONDS)
                            .thenApply(response -> {
                                if (response.statusCode() == 200) {
                                    _crawlResults.put(url.toString(), response.body());
                                }
                                return response.body();
                            })
                            .thenApply(path -> path))
                .collect(Collectors.toList());

        CompletableFuture.allOf(requests.toArray(new CompletableFuture[this._intranetDocuments.size()])).exceptionally( ex -> {
            System.out.println(ex);
            return null;
        }).join();
        System.out.println("done");
        return this;
    }

    public Map<String, String> getCrawlResults() {
        return this._crawlResults;
    }

    private void _setDocumentsToCrawl() throws MongoEntityException, URISyntaxException {
        Mongo mongo = new Mongo("naija_glasses_db");
        Datastore orm = mongo.getOrmInstance();
        List<DomLink> storedLinks = orm.createQuery(DomLink.class).asList(new FindOptions());

        Iterator iterator = storedLinks.iterator();
        ArrayList<URI> tempLinks = new ArrayList<>();
        while (iterator.hasNext()) {
            try {
                String href = ((DomLink)iterator.next()).link;
                String cleanedHref = href.replace("\"","").replace(" ","").trim();
                URI uri = new URI(cleanedHref);
                tempLinks.add(uri);
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        this._intranetDocuments.addAll(tempLinks);

    }
}
