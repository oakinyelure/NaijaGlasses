package Core.Controllers;
import spark.Response;
import spark.Request;

public abstract class ApiController<T> {

    public Response response;

    public Request request;
}
