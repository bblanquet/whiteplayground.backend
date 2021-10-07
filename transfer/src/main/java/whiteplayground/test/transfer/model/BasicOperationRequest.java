package whiteplayground.test.transfer.model;

import lombok.Data;

@Data
public class BasicOperationRequest {
    private String currency;
    private Double amount;
}
