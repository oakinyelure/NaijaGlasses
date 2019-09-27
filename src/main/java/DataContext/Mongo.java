package DataContext;

import DataContext.Exceptions.MongoEntityException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class Mongo {

    private MongoClient _mongoClient;

    private Datastore _dataStore;

    private Morphia _morphia;

    private DBCollection _collection;

    public Mongo(String dataStore) throws MongoEntityException {
        _mongoClient = new MongoClient("localhost",27017);
        _morphia = new Morphia();
        _morphia.mapPackage("DataContext.Models");
        this.createDataStore(dataStore);
    }

    /**
     * Todo check if entity exists already
     * @param storeName
     * @throws MongoEntityException
     */
    public void createDataStore(String storeName) throws MongoEntityException {
        try {
            this._dataStore = this._morphia.createDatastore(this._mongoClient,storeName);
        }
        catch (Exception ex) {
            throw new MongoEntityException();
        }
    }

    public void mapEntity(Class entity) {
        this._morphia.map(entity);
    }

    public Datastore getOrmInstance() {
        if(this._dataStore == null) {
            throw new NullPointerException("An instance of the datastore have not being created. Use @method createEntity");
        }
        return this._dataStore;
    }
}
