package whiteplayground.test.transfer.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import whiteplayground.test.transfer.api.repository.AccountRepository;
import whiteplayground.test.transfer.api.repository.CustomerRepository;
import whiteplayground.test.transfer.model.Account;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private CustomerRepository customerRepo;

    public Flux<Account> getAll(){
        this.customerRepo.findAll().map();
        return this.accountRepo.findAll();
    }
}
