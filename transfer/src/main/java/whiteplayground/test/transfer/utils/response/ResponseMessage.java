package whiteplayground.test.transfer.utils.response;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySources({@PropertySource("classpath:message.properties")})
public class ResponseMessage {
    @Value("${success}")
    private String success;

    @Value("${currency.notfound}")
    private String currencyNotFound;

    @Value("${notfound}")
    private String couldNotFind;

    @Value("${account.notfound}")
    private String accountNotFound;

    @Value("${alreadyExist}")
    private String alreadyExist;

    @Value("${database.delete}")
    private String couldNotDelete;

    @Value("${database.save}")
    private String couldNotSave;

    @Value("${login.error}")
    private String loginError;

    @Value("${transfer.error}")
    private String transferError;

    @Value("${account.money}")
    private String accountMoney;

    @Value("${negativeAmount}")
    private String negativeAmount;

    @Value("${rate.notfound}")
    private String rateNotFound;
}
