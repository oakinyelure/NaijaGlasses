import Core.Route;

import static spark.Spark.awaitInitialization;
import static spark.Spark.port;

public class Entry {
    public static void main(String[] args) {
        String portNumber = System.getProperty("PORT");
        port(8080);

        if(portNumber != null) {
            port(Integer.parseInt(portNumber));
        }
        Route.render();

        awaitInitialization();
    }
}
