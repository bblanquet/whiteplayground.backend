package whiteplayground.test.transfer;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import reactor.test.StepVerifier;
import whiteplayground.test.transfer.model.RateRequest;
import whiteplayground.test.transfer.model.RateResponse;
import whiteplayground.test.transfer.utils.http.HttpRequesterImpl;
import whiteplayground.test.transfer.utils.http.HttpResponseReaderImpl;
import whiteplayground.test.transfer.utils.json.JsonConverterImpl;

public class HttpSenderTests {

    @Test
    public void should_receive_the_correct_rate() {
        var url = "https://api.exchangerate.host/latest";
        var payload = new RateRequest("USD","EUR");
        var jsonConverter = new JsonConverterImpl<RateResponse>(RateResponse.class);
        var reader = new HttpResponseReaderImpl();
        var sender = new HttpRequesterImpl<RateRequest, RateResponse>(jsonConverter,reader);
        var response = sender.get(url,payload);

        StepVerifier.create(response)
                .consumeNextWith(result->{
                    var value = result.getValue();
                    Assert.isTrue(value.base.equals("USD"),"should get a response having base = USD");
                    Assert.isTrue(value.rates.containsKey("EUR"),"should get a response having rate = USD");
                }).verifyComplete();
    }
}
