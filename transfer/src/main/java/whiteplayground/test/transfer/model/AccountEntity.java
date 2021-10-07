package whiteplayground.test.transfer.model;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@Table("account")
public class AccountEntity {
    @Id
    private long id;
    @Column("customer_id")
    private long customer_id;
    @Column("currency")
    private String currency;
    @Column("date")
    private Instant date;
    @Column("is_deleted")
    private boolean isDeleted;
}

