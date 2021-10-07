package whiteplayground.test.transfer.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Deal {
    private String counterpart;
    private Date date;
    private OperationKind kind;
    private Double amount;
    private String currency;
    private Double rate;
}
