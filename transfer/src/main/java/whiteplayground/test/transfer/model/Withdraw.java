package whiteplayground.test.transfer.model;
import lombok.Data;
import java.time.Instant;

@Data
public class Withdraw {
    private Instant Date;
    private Double amount;
}
