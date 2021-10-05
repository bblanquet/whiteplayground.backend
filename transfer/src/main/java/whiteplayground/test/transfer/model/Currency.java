package whiteplayground.test.transfer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {
    @JsonProperty("code")
    public String code;
    @JsonProperty("description")
    public String description;
}
