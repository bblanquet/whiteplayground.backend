package whiteplayground.test.transfer.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface JsonConverter<T> {
    T deserialize(String value) throws JsonProcessingException;
    String serialize(T obj) throws JsonProcessingException;
}
