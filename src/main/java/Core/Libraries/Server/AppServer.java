package Core.Libraries.Server;

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
}
