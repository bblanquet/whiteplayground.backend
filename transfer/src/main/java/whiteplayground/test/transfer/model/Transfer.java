package whiteplayground.test.transfer.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;

@Data
public class Transfer {
    private String emitter;
    private String receiver;
    @Column("receiver_id")
    private long receiverId;
    @Column("emitter_id")
    private long emitterId;
    @Column("emitted_amount")
    private Double emittedAmount;
    @Column("emitted_currency")
    private String emittedCurrency;
    @Column("received_amount")
    private Double receivedAmount;
    @Column("received_currency")
    private String receivedCurrency;
    private Double rate;
    private Instant date;

}
