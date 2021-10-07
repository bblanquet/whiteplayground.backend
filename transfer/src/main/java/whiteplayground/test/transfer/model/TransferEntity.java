package whiteplayground.test.transfer.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("deal")
@Data
@Builder(toBuilder = true)
public class TransferEntity {
    @Id
    private long id;
    @Column("emitter_id")
    private long emitterId;
    @Column("receiver_id")
    private long receiverId;
    @Column("date")
    private Instant date;
    @Column("emitted_currency")
    private String emittedCurrency;
    @Column("received_currency")
    private String receivedCurrency;
    @Column("emitted_amount")
    private Double emittedAmount;
    @Column("received_amount")
    private Double receivedAmount;
    @Column("rate")
    private Double rate;
}
