package Core.EngineModules.Dom;

import Core.EngineModules.Crawlers.RestCrawl;
import DataContext.Exceptions.MongoEntityException;
import DataContext.Models.DomIndexedContent;
import DataContext.Models.DomLink;
import DataContext.Mongo;
import dev.morphia.Datastore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URISyntaxException;
import java.util.*;

public class DomParser {

    private RestCrawl _crawler;

    private List<DomNode> _webContentNodes;

    private Map<String, DomIndexedContent> _indexedContent;

    public DomParser() throws URISyntaxException, MongoEntityException {
        this._crawler = new RestCrawl();
        this._webContentNodes = new ArrayList<DomNode>();
        this._indexedContent = new HashMap<String, DomIndexedContent>();
    }

    private void _makeDocumentNode(Element element, String sourceUrl) {
        if(element == null) {
            return;
        }
        if(element.children().size() == 0) {
            DomNode nodeContent = new DomNode();
            if(element.hasAttr("src")) {
                nodeContent.setImageLink(element.attr("src"));
            }
            if(element.hasAttr("href")) {
                nodeContent.setOuterLink(element.attr("href"));
            }
            if(element.tagName() == "title") {
                nodeContent.setTitle(element.text());
            }
            nodeContent.setTextContent(element.text());
            nodeContent.setUrl(sourceUrl);
            if(_webContentNodes.size() > 0) {
                nodeContent.setParentNode(this._webContentNodes.get(this._webContentNodes.size() - 1));
            }
            this._webContentNodes.add(nodeContent);
            return;
        }
        else {
            for(Element childElement: element.children()) {
                this._makeDocumentNode(childElement,sourceUrl);
            }
        }

    }

    public DomParser parseAsync() {
        this._crawler.initAsync();
        Map<String,String> resultSet = this._crawler.getCrawlResults();
        for(Map.Entry<String, String> item: resultSet.entrySet()) {
            String sourceUrl = item.getKey();
            String htmlContent = item.getValue();
            Document dom = Jsoup.parse(htmlContent);
            Element documentBody = dom.body();
            documentBody.prepend(String.valueOf(dom.head()));
            for(Element element: documentBody.children()) {
                this._makeDocumentNode(element, sourceUrl);
            }
        }
        return this;
    }

    public Map<String, DomIndexedContent> getTest() {
        return this._indexedContent;
    }

    public DomParser initIndexing() throws Exception {
        HashSet<DomLink> validLinks = new HashSet<>();
        for(DomNode node: this._webContentNodes) {
            if(node.getUrl() != null && node.getTextContent() != null && !node.getTextContent().isEmpty()) {
                DomIndexedContent content = new DomIndexedContent();
                if(node.getImageLink() != null) {
                    content.images.add(node.getImageLink());
                }
                if(node.getOuterLink() != null) {
                    content.links.add(node.getOuterLink().replace("\"","").replace(" ","").trim());
                    if(node.getOuterLink().startsWith("http") || node.getOuterLink().startsWith("https")) {
                        DomLink newLink = new DomLink();
                        newLink.link = node.getOuterLink().replace("\"","").replace(" ","").trim();
                        newLink.parentSite = node.getUrl().replace("\"","").replace(" ","").trim();
                        validLinks.add(newLink);
                    }
                }
                content.contents.add(node.getTextContent());
                content.url = node.getUrl();
                content.title = node.getTitle();

                if(this._indexedContent.containsKey(node.getUrl())) {
                    DomIndexedContent existingInstance = this._indexedContent.get(node.getUrl());
                    existingInstance.links.addAll(content.links);
                    existingInstance.images.addAll(content.images);
                    existingInstance.contents.addAll(content.contents);
                }
                else {

                    this._indexedContent.put(node.getUrl(),content);
                }
            }
        }

        if(validLinks.size() > 0) {
            this.saveIndexedLink(validLinks);
        }
        return this;
    }

    private void saveIndexedLink(HashSet<DomLink> links) throws Exception {
        Mongo mongo = new Mongo("naija_glasses_db");
        Datastore orm = mongo.getOrmInstance();
        List<DomLink> existingLinks = orm.createQuery(DomLink.class).find().toList();
        links.addAll(existingLinks);
        orm.delete(orm.createQuery(DomLink.class));
        orm.save(links);
    }

    public void saveIndexedContent() throws Exception {
        List<DomIndexedContent> webContent = new ArrayList<>(this._indexedContent.values());
        Mongo mongo = new Mongo("naija_glasses_db");
        mongo.getOrmInstance().delete(mongo.getOrmInstance().createQuery(DomIndexedContent.class));
        mongo.getOrmInstance().save(webContent);
    }



}
