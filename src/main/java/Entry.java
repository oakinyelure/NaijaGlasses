import Core.EngineModules.Dom.DomParser;
import Core.Libraries.Server.AppServer;
import Core.Route;
import java.net.URISyntaxException;
import static spark.Spark.*;

public class Entry {

    public static void main(String[] args) {
        try {
            DomParser parser = new DomParser();
            parser.parseAsync();
        }
        catch (URISyntaxException ex) {
            // Todo: Would probably send notification to admin that there is an issue with the parser
        }
        AppServer.init();
        Route.resolve();
        awaitInitialization();
    }
}
