package whiteplayground.test.transfer.security;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
    @Autowired
    private TokenManager tokenManager;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = tokenManager.getUsernameFromToken(authToken);
        return Mono.just(tokenManager.validateToken(authToken))
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(valid -> {
                    Claims claims = tokenManager.getAllClaimsFromToken(authToken);
                    List<String> rolesMap = claims.get("role", List.class);
                    return new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                    );
                });
    }
}