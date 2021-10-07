package whiteplayground.test.transfer.api.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import whiteplayground.test.transfer.model.Withdraw;
import whiteplayground.test.transfer.model.WithdrawEntity;

public interface WithdrawRepository extends R2dbcRepository<WithdrawEntity, Long> {
    @Query("select amount, date from withdraw where account_id = :number")
    Flux<Withdraw> getFromAccountId(long number);
}
