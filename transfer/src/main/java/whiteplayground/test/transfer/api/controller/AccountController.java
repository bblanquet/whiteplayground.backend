package whiteplayground.test.transfer.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import whiteplayground.test.transfer.api.service.AccountService;
import whiteplayground.test.transfer.model.Account;
import whiteplayground.test.transfer.model.SummaryAccount;

import java.sql.Date;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    private  AccountService accountSvc;

    @GetMapping("all")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "all accounts", security = @SecurityRequirement(name = "bearerAuth"))
    public Flux<SummaryAccount> getAllItems() {
        return accountSvc.getAll()
                .map(e->{
                    var a = new SummaryAccount();
                    a.setName(e.getCustomer().getName());
                    a.setDate(Date.from(e.getDate()));
                    a.setCurrency(e.getCurrency());
                    return a;
                });
    }
}
