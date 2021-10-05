package whiteplayground.test.transfer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import whiteplayground.test.transfer.model.CcyResponse;
import whiteplayground.test.transfer.utils.http.HttpRequesterParamLess;
import whiteplayground.test.transfer.utils.http.HttpRequesterParamLessImpl;
import whiteplayground.test.transfer.utils.http.HttpResponseReaderImpl;
import whiteplayground.test.transfer.utils.json.JsonConverterImpl;

@Configuration
public class Factory {
    @Bean
    HttpRequesterParamLess<CcyResponse> CurrencyRequester(){
        var reader = new HttpResponseReaderImpl();
        var converter = new JsonConverterImpl<CcyResponse>(CcyResponse.class);
        var httpSender = new HttpRequesterParamLessImpl<CcyResponse>(converter, reader);
        return httpSender;
    }

}
