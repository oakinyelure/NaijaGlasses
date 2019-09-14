import Core.Libraries.Server.AppServer;
import Core.Route;

import static spark.Spark.awaitInitialization;

public class Entry {
    public static void main(String[] args) {
        AppServer.init();
        Route.resolve();
        awaitInitialization();
    }
}
