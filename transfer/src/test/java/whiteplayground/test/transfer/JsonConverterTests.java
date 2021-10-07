package whiteplayground.test.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import whiteplayground.test.transfer.model.RateResponse;
import whiteplayground.test.transfer.utils.json.JsonConverterImpl;

public class JsonConverterTests {
    @Test
    public void should_serialize_correctly() throws JsonProcessingException {
        var content = "{\"motd\":{\"msg\":\"If you or your company use this project or like what we doing, please consider backing us so we can continue maintaining and evolving this project.\",\"url\":\"https://exchangerate.host/#/donate\"},\"success\":true,\"base\":\"AED\",\"date\":\"2021-09-29\",\"rates\":{\"USD\":0.272148}}";
        var jsonConverter = new JsonConverterImpl<RateResponse>(RateResponse.class);
        var response = jsonConverter.deserialize(content);
        Assert.isTrue(response.base.equals("AED"), "should retrieve AED base.");
        Assert.isTrue(response.rates.containsKey("USD"), "should retrieve USD rate.");
    }
}
