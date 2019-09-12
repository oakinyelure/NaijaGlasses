package DataContext;

import DataContext.Exceptions.MongoEntityException;
import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class Mongo {

    private MongoClient _mongoClient;

    private Datastore _dataStore;

    private Morphia _morphia;

    Mongo() {
        _mongoClient = new MongoClient("localhost",27017);
        _morphia = new Morphia();
        _morphia.mapPackage("com.naijaglasses.entity");
    }

    /**
     * Todo check if entity exists already
     * @param storeName
     * @throws MongoEntityException
     */
    public void createEntity(String storeName) throws MongoEntityException {
        try {
            this._dataStore = this._morphia.createDatastore(this._mongoClient,storeName);
        }
        catch (Exception ex) {
            throw new MongoEntityException();
        }

    }
}
