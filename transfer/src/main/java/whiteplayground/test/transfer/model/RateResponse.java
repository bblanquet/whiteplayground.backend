package whiteplayground.test.transfer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RateResponse {
    @JsonProperty("success")
    public boolean success;
    @JsonProperty("base")
    public String base;
    @JsonProperty("date")
    public String date;
    @JsonProperty("rates")
    public Map<String, Double> rates;
}
