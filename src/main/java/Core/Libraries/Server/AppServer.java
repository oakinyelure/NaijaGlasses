package Core.Libraries.Server;

import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.before;
import static spark.Spark.port;

public abstract class AppServer {

    public static void init() {
        String portNumber = System.getProperty("PORT");
        if(portNumber == null) {
            port(8081);
        }
        else {
            port(Integer.parseInt(portNumber));
        }
    }

    public static void enableCORS(final String origin, final String methods, final String headers) {
        before(new Filter() {
            @Override
            public void handle(Request request, Response response) {
                response.header("Access-Control-Allow-Origin", origin);
                response.header("Access-Control-Request-Method", methods);
                response.header("Access-Control-Allow-Headers", headers);
            }
        });
    }
}
