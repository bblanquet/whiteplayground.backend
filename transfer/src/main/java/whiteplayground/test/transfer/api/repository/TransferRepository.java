package whiteplayground.test.transfer.api.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import whiteplayground.test.transfer.model.Transfer;
import whiteplayground.test.transfer.model.TransferEntity;

public interface TransferRepository extends R2dbcRepository<TransferEntity, Long> {
    @Query("SELECT em_customer.name as emitter, re_customer.name as receiver, deal.emitter_id, deal.receiver_id, deal.date, deal.emitted_amount, deal.emitted_currency, deal.received_amount, deal.received_currency, deal.rate FROM deal INNER JOIN account as em_account ON deal.emitter_id = em_account.id INNER JOIN customer as em_customer on em_customer.id = em_account.customer_id INNER JOIN account as re_account ON deal.receiver_id = re_account.id INNER JOIN customer as re_customer on re_customer.id = re_account.customer_id")
    Flux<Transfer> getAllDeals();
}
