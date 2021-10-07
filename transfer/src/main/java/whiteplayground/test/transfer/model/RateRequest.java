package whiteplayground.test.transfer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateRequest {
    private String base;
    private String symbols;
}
