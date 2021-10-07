package whiteplayground.test.transfer.api.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.api.repository.AccountRepository;
import whiteplayground.test.transfer.model.AccountDetails;
import whiteplayground.test.transfer.model.AccountEntity;
import whiteplayground.test.transfer.model.Account;
import whiteplayground.test.transfer.utils.response.ResponseMessage;
import whiteplayground.test.transfer.utils.response.EmptyResponse;
import java.time.Instant;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private CustomerService customerSvc;
    @Autowired
    private CurrencyService currencySvc;
    @Autowired
    private DealService dealSvc;
    @Autowired
    private ResponseMessage messages;

    public Mono<Instant> getCreationDate(String name, String currency){
        return this.accountRepo.getAccount(name,currency).map(c->c.getDate());
    }

    public Mono<Boolean> exist(String name, String currency){
        return this.accountRepo.exitByNameAndCurrency(name,currency);
    }

    public Mono<Long> getId(String name, String currency){
        return this.accountRepo.getId(name,currency);
    }

    public Mono<Account> getAccount(String name, String currency){
        return this.accountRepo.getAccount(name,currency);
    }

    public Flux<AccountDetails> GetAllFromName(String name){
        return this.accountRepo.getAllByCustomerName(name)
                .flatMap(acc->
                        this.dealSvc.getAmount(acc.getName(),acc.getCurrency())
                                .flatMap(amount->
                                        Mono.just(AccountDetails
                                                .builder()
                                                .amount(amount)
                                                .name(acc.getName())
                                                .date(acc.getDate())
                                                .currency(acc.getCurrency())
                                                .build()))
                );
    }

    public Flux<Account> getAll(){
        return this.accountRepo.getAll();
    }

    public Mono<EmptyResponse> delete(String name, String currency) {
        return customerSvc.existByUsername(name)
                .flatMap(existCustomer->{
                    if(existCustomer){
                        return this.currencySvc.exist(currency)
                                .flatMap(existCcy->{
                                    if(existCcy){
                                        return this.exist(name,currency)
                                                .flatMap(existAccount->{
                                                    if(existAccount){
                                                        return this.getId(name,currency)
                                                                .flatMap(id->{
                                                                    return this.accountRepo.disable(id)
                                                                            .flatMap(e->Mono.just(new EmptyResponse(true,messages.getSuccess())))
                                                                            .switchIfEmpty(Mono.just(new EmptyResponse(false,messages.getCouldNotDelete())));});
                                                    }else{
                                                        return Mono.just(new EmptyResponse(false,String.format(messages.getAccountNotFound(),name,currency)));
                                                    }
                                                });
                                    } else{
                                        return Mono.just(new EmptyResponse(false,String.format(messages.getCouldNotFind(),currency)));
                                    }
                                });
                    }else{
                        return Mono.just(new EmptyResponse(false,String.format(messages.getCouldNotFind(),name)));
                    }
                });
    }


    public Mono<EmptyResponse> add(String name, String currency) {
        return customerSvc.existByUsername(name)
                .flatMap(existCustomer->{
                    if(existCustomer){
                        return this.currencySvc.exist(currency)
                                .flatMap(existCcy->{
                                    if(existCcy){
                                        return this.exist(name,currency)
                                                .flatMap(existAccount->{
                                                    if(existAccount){
                                                        return Mono.just(new EmptyResponse(false,String.format(messages.getAlreadyExist(),name,currency)));
                                                    }else{
                                                        return this.customerSvc.getId(name)
                                                                .flatMap(id->{
                                                                    var customer = AccountEntity
                                                                            .builder()
                                                                            .customer_id(id)
                                                                            .currency(currency)
                                                                            .isDeleted(false)
                                                                            .date(Instant.now())
                                                                            .build();
                                                                    return accountRepo.save(customer)
                                                                            .flatMap(e->Mono.just(new EmptyResponse(true,messages.getSuccess())))
                                                                            .switchIfEmpty(Mono.just(new EmptyResponse(false,messages.getCouldNotSave())));
                                                                });
                                                    }
                                                });
                                    } else{
                                        return Mono.just(new EmptyResponse(false,String.format(messages.getCouldNotFind(),currency)));
                                    }
                                });
                    }else{
                        return Mono.just(new EmptyResponse(false,String.format(messages.getCouldNotFind(),name)));
                    }
                });
    }
}
