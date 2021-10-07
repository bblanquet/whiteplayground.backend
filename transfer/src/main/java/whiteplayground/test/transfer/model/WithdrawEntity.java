package whiteplayground.test.transfer.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@Table("withdraw")
public class WithdrawEntity {
    @Id
    private long id;
    @Column("account_id")
    private long accountId;
    @Column("amount")
    private double amount;
    @Column("date")
    private Instant date;
}
