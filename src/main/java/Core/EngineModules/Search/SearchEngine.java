package Core.EngineModules.Search;

import DataContext.Exceptions.MongoEntityException;
import DataContext.Models.DomIndexedContent;
import DataContext.Mongo;
import dev.morphia.Datastore;
import dev.morphia.query.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class SearchEngine {
    private Mongo _mongoClient;

    public SearchEngine() throws MongoEntityException {
        this._mongoClient = new Mongo("naija_glasses_db");
    }

    public List get(String keyword) {
        List<String> keywordParts = Arrays.asList(keyword.split(" "));
        Datastore data = this._mongoClient.getOrmInstance();
        ArrayList<DomIndexedContent> webContents = (ArrayList<DomIndexedContent>) data.createQuery(DomIndexedContent.class).find().toList();
        List<DomIndexedContent> rankedResults = this.rank(keywordParts,webContents);
        return webContents;
    }

    private List<DomIndexedContent> rank(List<String> searchEntities, ArrayList<DomIndexedContent> indexedContent) {
        for(int i = 0; i < indexedContent.size(); i++) {
            HashSet<String> currentContent = indexedContent.get(i).contents;
            int currentIndex = i;
            currentContent.forEach(content -> {
                List<String> delimitedContent = Arrays.asList(content.split(" "));
                List<String> commonContent = delimitedContent.stream().filter(searchEntities::contains).collect(Collectors.toList());
                if(commonContent.size() > 0) {
                    indexedContent.get(currentIndex).searchHitCount++;
                }
            });
        }
        // sort in ascending order. Highest search hit first
        indexedContent.sort((current, next) -> {
            return next.searchHitCount - current.searchHitCount;
        });
        return indexedContent;
    }
}
