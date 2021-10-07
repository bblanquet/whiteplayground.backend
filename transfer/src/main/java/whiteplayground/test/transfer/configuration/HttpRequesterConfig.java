package whiteplayground.test.transfer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import whiteplayground.test.transfer.model.CcyResponse;
import whiteplayground.test.transfer.model.RateRequest;
import whiteplayground.test.transfer.model.RateResponse;
import whiteplayground.test.transfer.utils.http.*;
import whiteplayground.test.transfer.utils.json.JsonConverterImpl;

@Configuration
public class HttpRequesterConfig {
    @Bean
    HttpRequesterParamLess<CcyResponse> currencyRequester(){
        var reader = new HttpResponseReaderImpl();
        var converter = new JsonConverterImpl<CcyResponse>(CcyResponse.class);
        var httpSender = new HttpRequesterParamLessImpl<CcyResponse>(converter, reader);
        return httpSender;
    }

    @Bean
    HttpRequester<RateRequest, RateResponse> rateRequester(){
        var reader = new HttpResponseReaderImpl();
        var converter = new JsonConverterImpl<RateResponse>(RateResponse.class);
        var httpSender = new HttpRequesterImpl<RateRequest,RateResponse>(converter, reader);
        return httpSender;
    }

}
