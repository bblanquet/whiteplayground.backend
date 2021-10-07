package whiteplayground.test.transfer.api.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.model.Account;
import whiteplayground.test.transfer.model.AccountEntity;

public interface AccountRepository extends R2dbcRepository<AccountEntity, Long> {
    @Query("SELECT account.id, customer.name as name, account.date as date, account.currency FROM account INNER JOIN customer ON account.customer_id = customer.id WHERE account.is_deleted = FALSE")
    Flux<Account> getAll();

    @Query("SELECT account.id, customer.name as name, account.date as date, account.currency FROM account INNER JOIN customer ON account.customer_id = customer.id WHERE customer.name = :name AND account.is_deleted = FALSE")
    Flux<Account> getAllByCustomerName(String name);

    @Query("SELECT account.id  FROM account INNER JOIN customer ON account.customer_id = customer.id WHERE customer.name = :name and account.currency = :currency AND account.is_deleted = FALSE")
    Mono<Long> getId(String name, String currency);

    @Query("SELECT account.id, customer.name as name, account.date as date, account.currency FROM account INNER JOIN customer ON account.customer_id = customer.id WHERE customer.name = :name and account.currency = :currency AND account.is_deleted = FALSE")
    Mono<Account> getAccount(String name, String currency);

    @Query("SELECT exists(SELECT * FROM customer INNER JOIN account ON account.customer_id = customer.id WHERE account.currency = :currency AND customer.name = :name AND account.is_deleted = FALSE)")
    Mono<Boolean> exitByNameAndCurrency(String name, String currency);

    @Modifying
    @Query("UPDATE account SET is_deleted = TRUE WHERE account.id = :id")
    Mono<Integer> disable(Long id);
}
