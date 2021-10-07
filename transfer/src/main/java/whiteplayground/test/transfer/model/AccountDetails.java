package whiteplayground.test.transfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AccountDetails {
    @JsonIgnore
    private long id;
    private String name;
    private String currency;
    private Instant date;
    private Double amount;
}
