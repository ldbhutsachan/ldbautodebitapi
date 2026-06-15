package unitl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Asus
 */
@Slf4j
public class JsonMapper {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String toSimplifyString(Object data) throws JsonProcessingException {
        return MAPPER.writeValueAsString(data);
    }

    public static String toJsonString(Object data) throws JsonProcessingException {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(data);
    }
    public static String mapToSimpleString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "";
        }
    }
}
