package Core;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonSerializer {

    /**
     * Converts an object to JSOn
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    /**
     * Converts from json string to the model
     * @param jsonString
     * @param model
     * @param <T>
     * @return
     */
    public static <T> T jsonTo(String jsonString, Class<T> model) {
        return new Gson().fromJson(jsonString,model);
    }

    public static ResponseTransformer json(){
        return JsonSerializer::toJson;
    }
}
