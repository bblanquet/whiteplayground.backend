package whiteplayground.test.transfer.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.api.repository.DepositRepository;
import whiteplayground.test.transfer.api.repository.TransferRepository;
import whiteplayground.test.transfer.api.repository.WithdrawRepository;
import whiteplayground.test.transfer.model.*;
import whiteplayground.test.transfer.utils.response.EmptyResponse;
import whiteplayground.test.transfer.utils.response.ResponseMessage;

import java.time.Instant;

@Service
public class OperationService {
    @Autowired
    private WithdrawRepository withdrawRepo;
    @Autowired
    private DepositRepository depositRepo;
    @Autowired
    private TransferRepository transferRepo;
    @Autowired
    private AccountService accountSvc;
    @Autowired
    private CustomerService customerSvc;
    @Autowired
    private CurrencyService currencySvc;
    @Autowired
    private DealService dealSvc;
    @Autowired
    private ResponseMessage messages;

    public Mono<EmptyResponse> transfer(String name, TransferRequest request){
        return this.isAmountAboveZero(request.getAmount(),
                this.existAccount(name,request.getEmittedCurrency(),
                        this.existAccount(request.getReceiver(),request.getReceivedCurrency(),
                                this.isDifferent(name,request,this.unsafeTransfer(name,request))
                        )));
    }

    public Mono<EmptyResponse> isDifferent(String name, TransferRequest request,  Mono<EmptyResponse> operation){
        if(name.equals(request.getReceiver()) && request.getReceivedCurrency().equals(request.getEmittedCurrency())){
            return Mono.just(new EmptyResponse(false,messages.getTransferError()));
        }else{
            return operation;
        }
    }

    public Mono<EmptyResponse> withdraw(String name, BasicOperationRequest request){
        return
                this.isAmountAboveZero(request.getAmount(),
                        this.existAccount(name, request.getCurrency(),
                                this.unsafeWithdraw(name,request)));
    }

    public Mono<EmptyResponse> deposit(String name, BasicOperationRequest request){
        return
                this.isAmountAboveZero(request.getAmount(),
                        this.existAccount(name, request.getCurrency(),
                                this.unsafeDeposit(name,request)));
    }

    private Mono<EmptyResponse> unsafeTransfer(String name, TransferRequest request){
        return dealSvc.getAmount(name,request.getEmittedCurrency())
                .flatMap(amount->{
                    if(amount < request.getAmount()){
                        return Mono.just(new EmptyResponse(false,String.format(messages.getAccountMoney(),name,request.getEmittedCurrency())));
                    }
                    else{
                        var req = new RateComparisonRequest(request.getEmittedCurrency(),request.getReceivedCurrency());
                        return currencySvc.getRate(req)
                                .flatMap(rate->{
                                    if(rate.isSuccess()){
                                        var emitterId = accountSvc.getId(name, request.getEmittedCurrency());
                                        var receivedId = accountSvc.getId(request.getReceiver(), request.getReceivedCurrency());
                                        return emitterId.zipWith(receivedId,(e,r)->{
                                            var entity = TransferEntity
                                                    .builder()
                                                    .emitterId(e)
                                                    .receiverId(r)
                                                    .emittedAmount(request.getAmount())
                                                    .receivedAmount(request.getAmount()*rate.getValue().getRate())
                                                    .emittedCurrency(request.getEmittedCurrency())
                                                    .receivedCurrency(request.getReceivedCurrency())
                                                    .rate(rate.getValue().getRate())
                                                    .date(Instant.now())
                                                    .build();
                                            return transferRepo.save(entity)
                                                    .flatMap(ent->Mono.just(new EmptyResponse(true,messages.getSuccess())))
                                                    .switchIfEmpty(Mono.just(new EmptyResponse(false,messages.getCouldNotSave())));
                                        }).flatMap(e->e);
                                    }else{
                                        return Mono.just(new EmptyResponse(false,messages.getRateNotFound()));
                                    }
                                });
                    }
                });
    }

    private Mono<EmptyResponse> unsafeDeposit(String name, BasicOperationRequest request){
        return accountSvc.getAccount(name,request.getCurrency())
                .flatMap(acc->{
                    var entity = DepositEntity
                            .builder()
                            .accountId(acc.getId())
                            .amount(request.getAmount())
                            .date(Instant.now())
                            .build();
                    return depositRepo.save(entity)
                            .flatMap(e->Mono.just(new EmptyResponse(true,messages.getSuccess())))
                            .switchIfEmpty(Mono.just(new EmptyResponse(false,messages.getCouldNotSave())));
                });
    }

    private Mono<EmptyResponse> unsafeWithdraw(String name, BasicOperationRequest request) {
        return dealSvc.getAmount(name,request.getCurrency())
                .flatMap(amount->{
                    if(amount < request.getAmount()){
                        return Mono.just(new EmptyResponse(false,String.format(messages.getAccountMoney(),name,request.getCurrency())));
                    }
                    else
                    {
                        return accountSvc.getAccount(name,request.getCurrency())
                                .flatMap(acc->{
                                    var entity = WithdrawEntity
                                            .builder()
                                            .accountId(acc.getId())
                                            .amount(request.getAmount())
                                            .date(Instant.now())
                                            .build();
                                    return withdrawRepo.save(entity)
                                            .flatMap(e->Mono.just(new EmptyResponse(true,messages.getSuccess())))
                                            .switchIfEmpty(Mono.just(new EmptyResponse(false,messages.getCouldNotSave())));
                                });
                    }
                });
    }

    private Mono<EmptyResponse> isAmountAboveZero(Double amount,Mono<EmptyResponse> operation){
        if(amount <= 0){
            return Mono.just(new EmptyResponse(false,messages.getNegativeAmount()));
        }else{
            return operation;
        }
    }

    private Mono<EmptyResponse> existAccount(String name, String currency, Mono<EmptyResponse> operation){
        return customerSvc.existByUsername(name)
                .flatMap(customerExist->{
                    if(!customerExist){
                        return Mono.just(new EmptyResponse(false,String.format(messages.getCouldNotFind(),name)));
                    }
                    else
                    {
                        return accountSvc.exist(name,currency)
                                .flatMap(accountExist->{
                                    if(!accountExist){
                                        return Mono.just(new EmptyResponse(false,String.format(messages.getAccountNotFound(),name,currency)));
                                    }
                                    else
                                    {
                                        return operation;
                                    }
                                });
                    }
                });
    }
}
