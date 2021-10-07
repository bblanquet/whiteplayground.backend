package whiteplayground.test.transfer.model;
import lombok.Data;
import java.time.Instant;

@Data
public class Deposit {
    private Instant Date;
    private Double amount;
}
