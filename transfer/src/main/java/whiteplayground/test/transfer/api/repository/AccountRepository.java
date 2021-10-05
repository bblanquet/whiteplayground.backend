package whiteplayground.test.transfer.api.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import whiteplayground.test.transfer.model.Account;

public interface AccountRepository extends R2dbcRepository<Account, Long> {
    @Query("SELECT * FROM account WHERE currency = :currency")
    Flux<Account> findByName(String currency);
}
