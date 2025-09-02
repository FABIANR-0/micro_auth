package co.com.pragma.api.config;

import co.com.pragma.api.exception.TokenValidationException;
import co.com.pragma.security.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {


    private final JwtProvider jwtProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        return Mono.just(token)
                .flatMap(this::validateAndCreateAuthentication)
                .doOnError(e -> log.warn("Token validation failed: {}", e.getMessage()))
                .flatMap(authentication -> {
                    SecurityContext securityContext = new SecurityContextImpl(authentication);
                    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                });
    }

    private Mono<Authentication> validateAndCreateAuthentication(String token) {
        return Mono.fromCallable(() -> {
            Claims claims = jwtProvider.validateTokenAndGetClaims(token);

            if (claims == null) {
                throw new TokenValidationException("Token no valido");
            }

            String role = (String) claims.get("role");
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
            return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
        });
    }
}