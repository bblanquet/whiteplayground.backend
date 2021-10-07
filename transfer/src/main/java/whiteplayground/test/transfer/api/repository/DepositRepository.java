package whiteplayground.test.transfer.api.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import whiteplayground.test.transfer.model.DepositEntity;
import whiteplayground.test.transfer.model.Withdraw;

public interface DepositRepository extends R2dbcRepository<DepositEntity, Long> {
    @Query("select amount, date from deposit where account_id = :number")
    Flux<Withdraw> getFromAccountId(long number);
}
