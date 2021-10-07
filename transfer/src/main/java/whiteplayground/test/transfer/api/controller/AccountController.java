package whiteplayground.test.transfer.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.api.service.AccountService;
import whiteplayground.test.transfer.api.service.DealService;
import whiteplayground.test.transfer.model.*;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    private AccountService accountSvc;

    @Autowired
    private DealService dealSvc;

    @GetMapping("all")
    public Flux<Account> getAll() {
        return accountSvc.getAll();
    }

    @DeleteMapping("delete")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "delete account", security = @SecurityRequirement(name = "bearerAuth"))
    public Mono<ResponseEntity<String>> delete(@RequestBody AccountRequest request) {
        return  getUsernameFromSecurityContext()
                .flatMap(name->
                        accountSvc.delete(name,request.getCurrency())
                                .map(result->{
                                    if(result.isSuccess()){
                                        return ResponseEntity.status(HttpStatus.OK)
                                                .contentType(MediaType.TEXT_PLAIN)
                                                .body("success");
                                    }else{
                                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                                                .contentType(MediaType.TEXT_PLAIN)
                                                .body(result.getMessage());
                                    }
                                }));
    }


    @PostMapping("add")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "add account", security = @SecurityRequirement(name = "bearerAuth"))
    public Mono<ResponseEntity<String>> add(@RequestBody AccountRequest request) {
        return  getUsernameFromSecurityContext()
                .flatMap(name->
                        accountSvc.add(name,request.getCurrency())
                                .map(result->{
                                    if(result.isSuccess()){
                                       return ResponseEntity.status(HttpStatus.OK)
                                                .contentType(MediaType.TEXT_PLAIN)
                                                .body("success");
                                    }else{
                                        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                                                .contentType(MediaType.TEXT_PLAIN)
                                                .body(result.getMessage());
                                    }
                                }));
    }



    @GetMapping("details")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "all accounts", security = @SecurityRequirement(name = "bearerAuth"))
    public Flux<Deal> getDetails(String currency) {
        return getUsernameFromSecurityContext().flatMapMany(e->dealSvc.getAllByAccount(e,currency));
    }

    @GetMapping("curve")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "all accounts", security = @SecurityRequirement(name = "bearerAuth"))
    public Flux<DateValue> getCurve(String currency) {
        return getUsernameFromSecurityContext().flatMapMany(e->dealSvc.getCurveByAccount(e,currency));
    }

    @GetMapping("list")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "all accounts", security = @SecurityRequirement(name = "bearerAuth"))
    public Flux<AccountDetails> getAllFromName() {
        return getUsernameFromSecurityContext().flatMapMany(e->this.accountSvc.GetAllFromName(e));
    }

    private Mono<String> getUsernameFromSecurityContext() {
        return ReactiveSecurityContextHolder
                .getContext()
                    .map(context -> context.getAuthentication().getName());
    }
}
