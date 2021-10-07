package whiteplayground.test.transfer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateComparisonRequest {
    private String currencyA;
    private String currencyB;
}
