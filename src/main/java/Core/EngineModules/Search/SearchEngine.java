package Core.EngineModules.Search;

import DataContext.Exceptions.MongoEntityException;
import DataContext.Models.DomIndexedContent;
import DataContext.Mongo;
import dev.morphia.Datastore;
import dev.morphia.query.Query;

import java.util.ArrayList;
import java.util.List;

public class SearchEngine {
    private Mongo _mongoClient;

    public SearchEngine() throws MongoEntityException {
        this._mongoClient = new Mongo("naija_glasses_db");
    }

    public List get(String keyword) {
        Datastore data = this._mongoClient.getOrmInstance();
        List searchResult = new ArrayList();
        Query<DomIndexedContent> query = data.createQuery(DomIndexedContent.class);
        return query.asList();
    }
}
