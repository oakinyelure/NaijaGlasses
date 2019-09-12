package DataContext.Exceptions;

public class MongoEntityException extends Exception {

    public MongoEntityException(String message) {
        super(message);
    }

    public MongoEntityException() {
        super("An error occured while manipulating mondgo entity");
    }
}
