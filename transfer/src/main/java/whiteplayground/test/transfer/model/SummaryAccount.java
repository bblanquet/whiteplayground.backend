package whiteplayground.test.transfer.model;

import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class SummaryAccount {
    private String name;
    private String currency;
    private Date date;
}
