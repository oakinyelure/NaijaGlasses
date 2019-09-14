package Core.Controllers;

public class SearchController extends ApiController {
    public SearchController() {}

    public Object Search() {
        try {
            response.status(404);
            return 2000;
        }
        catch (Exception ex) {
            //response.status(500);
            return "s";
        }
    }
}
