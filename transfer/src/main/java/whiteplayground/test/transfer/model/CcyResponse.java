package whiteplayground.test.transfer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CcyResponse {
    @JsonProperty("success")
    public boolean success;
    @JsonProperty("symbols")
    public Map<String, Currency> symbols;
}
