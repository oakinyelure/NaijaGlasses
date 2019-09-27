package Core.Controllers;


public class SearchController extends ApiController {
    public SearchController() {}

    public Object Search() {
        try {
            response.status(200);
            return 6;
        }
        catch (Exception ex) {
            response.status(500);
            return ex;
        }
    }
}
