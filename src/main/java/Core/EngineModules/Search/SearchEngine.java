package Core.EngineModules.Search;

import DataContext.Exceptions.MongoEntityException;
import DataContext.Models.DomIndexedContent;
import DataContext.Mongo;
import dev.morphia.Datastore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchEngine {
    private Mongo _mongoClient;

    public SearchEngine() throws MongoEntityException {
        this._mongoClient = new Mongo("naija_glasses_db");
    }

    public List get(String keyword) {
        List<String> keywordParts = Arrays.asList(keyword.split(" "));
        Datastore data = this._mongoClient.getOrmInstance();
        ArrayList<DomIndexedContent> webContents = (ArrayList<DomIndexedContent>) data.createQuery(DomIndexedContent.class).find().toList();
        for(DomIndexedContent content: webContents) {
            this.rank(keywordParts,content);
        }
        webContents.sort((current,next) -> {
            return next.searchHitCount - current.searchHitCount;
        });
        webContents.removeIf(c -> c.searchHitCount == 0);
        return webContents;
    }

    private void rank(List<String> searchEntities, DomIndexedContent indexedContent) {
        List<String> foundItems = new ArrayList<>();
        for(String text : indexedContent.contents) {
            for(int i = 0; i < searchEntities.size(); i++) {
                if(text.contains(searchEntities.get(i)) && !foundItems.contains(searchEntities.get(i))) {
                    indexedContent.searchHitCount = indexedContent.searchHitCount + 1;
                    foundItems.add(searchEntities.get(i));
                }
            }
        }
    }
}
