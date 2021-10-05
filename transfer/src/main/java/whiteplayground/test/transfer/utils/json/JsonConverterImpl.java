package whiteplayground.test.transfer.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverterImpl<T> implements JsonConverter<T> {
    private final Class<T> genericType;

    public JsonConverterImpl(Class<T> genericType) {
        this.genericType = genericType;
    }

    public T deserialize(String value) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        var complexValue = mapper.readValue(value, genericType);
        return (T) complexValue;
    }

    public String serialize(T obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writer().writeValueAsString(obj);
    }
}
