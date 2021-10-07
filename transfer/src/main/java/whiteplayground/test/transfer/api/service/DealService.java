package whiteplayground.test.transfer.api.service;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.api.repository.AccountRepository;
import whiteplayground.test.transfer.api.repository.TransferRepository;
import whiteplayground.test.transfer.api.repository.DepositRepository;
import whiteplayground.test.transfer.api.repository.WithdrawRepository;
import whiteplayground.test.transfer.model.DateValue;
import whiteplayground.test.transfer.model.Transfer;
import whiteplayground.test.transfer.model.OperationKind;
import whiteplayground.test.transfer.model.Deal;

import java.sql.Date;
import java.util.Comparator;

@Service
public class DealService {
    @Autowired
    private TransferRepository dealRepo;

    @Autowired
    private WithdrawRepository withdrawRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private DepositRepository depositRepo;

    @Autowired
    private AccountService accountSvc;

    public Flux<DateValue> getCurveByAccount(String name, String currency){
        var deals = this.getAllByAccount(name,currency).cache();

        var between =
                Mono.just(new CurrentContainer(0d))
                        .flatMapMany(v->
                                deals
                                        .map(deal->
                                        {
                                            v.value += deal.getAmount();
                                            return new DateValue(deal.getDate(), v.value);
                                        }));
        var start =
                accountSvc.getCreationDate(name,currency)
                        .map(d-> new DateValue(Date.from(d),0d))
                        .flatMapMany(e->Flux.just(e));

        var end = deals
                .map(e->e.getAmount())
                .reduce(0d, (x1, x2) -> x1 + x2)
                .map(e->new DateValue(new java.util.Date(System.currentTimeMillis()),e))
                .flatMapMany(d->Flux.just(d));
        return start.concatWith(between).concatWith(end);
    }

    public Mono<Double> getAmount(String name, String currency){
        return this.getAllByAccount(name,currency)
                .map(e->e.getAmount())
                .reduce(0d, (x1, x2) -> x1 + x2);
    }

    public Flux<Deal> getAllByAccount(String name, String currency) {
        return this.accountRepo.getId(name,currency)
                .flatMapMany(id->
                        Flux.concat(this.getTransfer(id)
                                        ,this.getWithdraw(name,currency)
                                        ,this.getDeposit(name,currency))
                                .sort(Comparator.comparing(Deal::getDate)));
    }


    private Flux<Deal> getWithdraw(String name, String currency){
        return accountRepo.getId(name,currency)
                .flatMapMany(id->withdrawRepo.getFromAccountId(id))
                .map(withdraw -> Deal
                        .builder()
                        .amount(withdraw.getAmount()*-1)
                        .rate(1d)
                        .kind(OperationKind.withdraw)
                        .counterpart("na")
                        .currency(currency)
                        .date(Date.from(withdraw.getDate())).build());
    }

    private Flux<Deal> getDeposit(String name, String currency){
        return accountRepo.getId(name,currency)
                .flatMapMany(id->depositRepo.getFromAccountId(id))
                .map(deposit -> Deal
                        .builder()
                        .amount(deposit.getAmount())
                        .rate(1d)
                        .kind(OperationKind.deposit)
                        .counterpart("na")
                        .currency(currency)
                        .date(Date.from(deposit.getDate())).build());
    }

    private Flux<Deal> getTransfer(long id) {
        return dealRepo.getAllDeals()
                .filter(deal -> this.hasDeal(id, deal))
                .map(deal -> {
                    if(this.isEmittedDeal(id,deal)){
                        return Deal
                                .builder()
                                .date(Date.from(deal.getDate()))
                                .kind(OperationKind.transfer)
                                .rate(deal.getRate())
                                .amount(deal.getEmittedAmount()*-1)
                                .counterpart(deal.getReceiver())
                                .currency(deal.getReceivedCurrency())
                                .build();
                    }else{
                        return Deal
                                .builder()
                                .date(Date.from(deal.getDate()))
                                .kind(OperationKind.transfer)
                                .rate(deal.getRate())
                                .amount(deal.getReceivedAmount())
                                .counterpart(deal.getEmitter())
                                .currency(deal.getEmittedCurrency())
                                .build();
                    }
                });
    }

    private boolean hasDeal(long id, Transfer deal){
        return this.isEmittedDeal(id,deal)
                || this.isReceivedDeal(id,deal) ;
    }

    private boolean isEmittedDeal(long id, Transfer deal){
        return deal.getEmitterId() == id;
    }

    private boolean isReceivedDeal(long id, Transfer deal){
        return deal.getReceiverId() == id;
    }

    @Data
    @AllArgsConstructor
    public class CurrentContainer{
        private Double value;
    }
}
