package whiteplayground.test.transfer.security;

import java.io.Serializable;

public class AuthenticateResponse implements Serializable {

    private final String token;

    public AuthenticateResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}