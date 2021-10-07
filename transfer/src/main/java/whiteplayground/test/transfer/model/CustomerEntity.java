package whiteplayground.test.transfer.model;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder(toBuilder = true)
@Table("customer")
public class CustomerEntity {
    @Id
    private long id;
    @Column("name")
    private String name;
    @Column("password")
    private String password;
}
