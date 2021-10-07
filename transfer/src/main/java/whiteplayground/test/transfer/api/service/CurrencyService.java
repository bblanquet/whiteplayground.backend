package whiteplayground.test.transfer.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.model.*;
import whiteplayground.test.transfer.utils.http.HttpRequester;
import whiteplayground.test.transfer.utils.http.HttpRequesterParamLess;
import whiteplayground.test.transfer.utils.response.ResponseMessage;
import whiteplayground.test.transfer.utils.response.Response;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
    //cache currencies to avoid multiple requests;
    private static List<Currency> currencies;
    @Value("${url.currency}")
    private String currencyUrl;
    @Value("${url.rate}")
    private String rateUrl;
    @Autowired
    private HttpRequester<RateRequest,RateResponse> rateRequester;
    @Autowired
    private HttpRequesterParamLess<CcyResponse> ccyRequester;
    @Autowired
    private ResponseMessage messages;

    public Mono<Response<RateComparisonResponse>> getRate(RateComparisonRequest request){
        var existA = this.exist(request.getCurrencyA());
        var existB = this.exist(request.getCurrencyB());
        return existA.concatWith(existB)
                .all(exist->exist)
                .flatMap(allExist->{
                    if(!allExist){
                        return Mono.just(Response.<RateComparisonResponse>error(messages.getCurrencyNotFound()));
                    }
                    if(request.getCurrencyA().equals(request.getCurrencyB())){
                        return Mono.just(Response.success(new RateComparisonResponse(1d)));
                    }else{
                        var payload = new RateRequest(request.getCurrencyA(), request.getCurrencyB());
                        return this.rateRequester.get(this.rateUrl, payload).map(result -> {
                            if (result.isSuccess()) {
                                var rate = result.getValue().rates.get(request.getCurrencyB());
                                return Response.<RateComparisonResponse>success(new RateComparisonResponse(rate));
                            } else {
                                return Response.<RateComparisonResponse>error(result.getMessage());
                            }
                        });
                    }
                });
    }

    public Mono<Boolean> exist(String currencyCode){
        return this.getAll()
                .filter(r->r.isSuccess())
                .flatMap(r-> Flux.fromIterable(r.getValue())
                        .any(i->i.code.equals(currencyCode)))
                .switchIfEmpty(Mono.just(false));
    }

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
