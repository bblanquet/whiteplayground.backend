package whiteplayground.test.transfer.model;

import lombok.Data;

@Data
public class TransferRequest {
    private String receiver;
    private String emittedCurrency;
    private String receivedCurrency;
    private Double amount;
}
