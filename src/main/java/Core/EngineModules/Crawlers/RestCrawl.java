package Core.EngineModules.Crawlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RestCrawl {
    private List<URI> _intranetDocuments = Arrays.asList(
            new URI("https://www.grantham.edu/"),
            new URI("https://www.grantham.edu/degree-type/masters-programs/"),
            new URI("https://www.iupui.edu/about/index.html"),
            new URI("https://www.grantham.edu/online-degrees/"),
            new URI("https://www.grantham.edu/graduation/"),
            new URI("https://www.ivytech.edu/about/index.html"),
            new URI("https://www.grantham.edu/about-grantham/"),
            new URI("https://www.grantham.edu/online-college-tuition/"),
            new URI("http://www.promoplace.com/granthamgear/"),
            new URI("https://www.grantham.edu/contact-us/"),
            new URI("https://www.grantham.edu/blog/"),
            new URI("https://www.grantham.edu/faqs/")
    );

    private HashMap _crawlResults;

    private HttpClient _http;

    public RestCrawl() throws URISyntaxException {
        this._http = HttpClient.newHttpClient();
        this._crawlResults = new HashMap();
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
}
