package unitl;

import com.google.gson.Gson;

/**
 * This is a Helper class for convert 2 type of datas
 *
 * @author KHAMPHAI
 */
public class JSONUtils {

    /**
     * Initial Gson converter
     */
    private static final Gson GSON = new Gson();
//    private static final Gson GSON = new GsonBuilder()
//            .disableHtmlEscaping()
//            .serializeNulls()
//            .create();

    /**
     * Defualt constuctor
     */
    public JSONUtils() {
    }

    /**
     * Convert JSON Object to String value
     *
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        return GSON.toJson(object);
    }

    /**
     * Convert String value to JSON Object
     *
     * @param jsonString
     * @param objectType
     * @param <T>
     * @return
     */
    public static <T> T toObject(String jsonString, Class<T> objectType) {
        return GSON.fromJson(jsonString, objectType);
    }

}
