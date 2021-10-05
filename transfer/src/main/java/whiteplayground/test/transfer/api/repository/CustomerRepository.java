package whiteplayground.test.transfer.api.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.model.Customer;

public interface CustomerRepository extends R2dbcRepository<Customer, Long> {
    @Query("SELECT * FROM customer WHERE name = :name")
    Mono<Customer> findByName(String name);

    @Query("SELECT exists(SELECT * FROM customer WHERE name = :name)")
    Mono<Boolean> exitByName(String name);
}