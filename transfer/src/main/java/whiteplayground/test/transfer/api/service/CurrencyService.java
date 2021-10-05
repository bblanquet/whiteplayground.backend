package whiteplayground.test.transfer.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.model.CcyResponse;
import whiteplayground.test.transfer.model.Currency;
import whiteplayground.test.transfer.utils.http.HttpRequesterParamLess;
import whiteplayground.test.transfer.utils.response.Response;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
    //cache currencies to avoid multiple requests;
    private static List<Currency> currencies;
    @Value("${url.currency}")
    private String currencyUrl;

    @Autowired
    private HttpRequesterParamLess<CcyResponse> ccyRequester;

    public Mono<Response<List<Currency>>> getAll() {
        if(currencies != null){
            return Mono.just(Response.success(currencies));
        }else{
            return this.ccyRequester.get(this.currencyUrl).map(e->{
                if(e.isSuccess()){
                    this.currencies =
                            e.getValue()
                                    .symbols
                                    .values()
                                    .stream().collect(Collectors.toList());
                    return Response.success(currencies);
                }else{
                    return Response.error(e.getMessage());
                }
            });
        }
    }}
