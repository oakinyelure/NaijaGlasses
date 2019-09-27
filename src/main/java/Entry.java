import Core.EngineModules.Dom.DomParser;
import Core.Libraries.Server.AppServer;
import Core.Route;
import java.net.URISyntaxException;
import static spark.Spark.*;

public class Entry {

    public static void main(String[] args) {
        AppServer.init();
        Route.resolve();
        awaitInitialization();
        try {
            DomParser content = new DomParser()
                    .parseAsync();
            content.initIndexing().saveIndexedContent();
        }
        catch (URISyntaxException ex) {
            // Todo: Required Logging
        }
        catch (Exception ex) {
            // Todo: Required logging here
        }
    }
}
