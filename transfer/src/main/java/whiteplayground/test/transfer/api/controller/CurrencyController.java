package whiteplayground.test.transfer.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.api.service.CurrencyService;
import whiteplayground.test.transfer.model.Currency;
import whiteplayground.test.transfer.utils.response.Response;

import java.util.List;

@RestController
@RequestMapping("currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencySvc;
    @GetMapping("all")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "all accounts", security = @SecurityRequirement(name = "bearerAuth"))
    public Mono<Response<List<Currency>>> getAllItems() {
        return currencySvc.getAll();
    }
}
