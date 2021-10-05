package whiteplayground.test.transfer.security;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class HashEncoder {
    public String toSha256(String value){
        return Hashing.sha256()
                .hashString(value, StandardCharsets.UTF_8)
                .toString();
    }
}
