package whiteplayground.test.transfer.api.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.api.service.CurrencyService;
import whiteplayground.test.transfer.model.RateComparisonRequest;

@RestController
@RequestMapping("currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencySvc;

    @GetMapping(value = "/list")
    @ResponseBody
    public Mono<ResponseEntity<? extends Object>> getAll() {

        return currencySvc.getAll().map(r->{
            if(r.isSuccess()){
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(r.getValue());
            }else{
                return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(r.getMessage());
            }
        });
    }

    @GetMapping(value = "/rate")
    @ResponseBody
    public Mono<ResponseEntity<? extends Object>> getRate(RateComparisonRequest currency){
        var response = currencySvc.getRate(currency);
        return response.map(r->{
            if(r.isSuccess()){
                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(r.getValue());
            }else{
                return  ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(r.getMessage());
            }
        });
    }
}
