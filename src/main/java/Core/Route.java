package Core;

import static spark.Spark.*;

public class Route {
    /**
     * Keep all routes here. Do not provide implementation
     * details in the route itself. The route should call its own implementation details rather than
     * provide the logic
     * EXAMPLE: get("/all", () -> {
     *     CLASS.implement()
     * });
     *
     * do not do get("all", () -> {
     *     for(int i  = 0; i < 54545; i++) {
     *
     *     }
     * })
     */
    public static void resolve() {
        get("/test", (request, response) -> {
           return JsonSerializer.toJson(new Route());
        });
    }
}
