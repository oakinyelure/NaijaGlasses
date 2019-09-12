package Core.Controllers;

import Core.JsonSerializer;

public abstract class ApiController {

    public Object Ok(Object response) {
        return JsonSerializer.toJson(response);
    }
}
