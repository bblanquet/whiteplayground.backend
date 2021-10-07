package whiteplayground.test.transfer.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.api.service.CustomerService;
import whiteplayground.test.transfer.security.AuthenticateRequest;

import java.io.Serializable;

@RestController
@RequestMapping("auth")
public class AuthenticateController {

    @Autowired
    private CustomerService customerSvc;

    @PostMapping("signIn")
    public Mono<ResponseEntity<? extends Serializable>> signIn(@RequestBody AuthenticateRequest ar) {
        return this.customerSvc.signIn(ar).map(response -> {
            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.getValue());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.TEXT_HTML)
                        .body(response.getMessage());
            }
        });
    }

    @PostMapping("signUp")
    public Mono<ResponseEntity<? extends Serializable>> signUp(@RequestBody AuthenticateRequest ar) {
        return this.customerSvc.signUp(ar).map(response -> {
            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response.getValue());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.TEXT_HTML)
                        .body(response.getMessage());
            }
        });
    }

}
