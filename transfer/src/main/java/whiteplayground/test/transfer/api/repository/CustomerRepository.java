package whiteplayground.test.transfer.api.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.model.CustomerEntity;

public interface CustomerRepository extends ReactiveCrudRepository<CustomerEntity, Long> {
    @Query("SELECT * FROM customer WHERE name = :name")
    Mono<CustomerEntity> findByName(String name);

    @Query("SELECT exists(SELECT * FROM customer WHERE name = :name)")
    Mono<Boolean> exitByName(String name);
}