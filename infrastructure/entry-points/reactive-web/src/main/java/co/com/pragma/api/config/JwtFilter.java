package co.com.pragma.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter {//implements WebFilter {
//    private final JwtAdapter jwtAdapter;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return chain.filter(exchange);
//        }
//
//        String token = authHeader.substring(7);
//
//        return Mono.just(token)
//                .flatMap(this::validateAndCreateAuthentication)
//                .flatMap(authentication -> {
//                    Context securityContext = ReactiveSecurityContextHolder
//                            .withSecurityContext(Mono.just(new SecurityContextImpl(authentication)));
//                    return chain.filter(exchange).contextWrite(securityContext);
//                })
//                .onErrorResume(e -> {
//                    log.warn("Token validation failed: {}", e.getMessage());
//                    return chain.filter(exchange);
//                });
//    }
//
//    private Mono<Authentication> validateAndCreateAuthentication(String token) {
//        return Mono.fromCallable(() -> {
//            Claims claims = jwtAdapter.validateTokenAndGetClaims(token);
//            String username = claims.getSubject();
//
//            if (username == null) {
//                throw new TokenNoValidException();
//            }
//
//            List<String> roles = jwtAdapter.extractRoles(claims);
//            List<SimpleGrantedAuthority> authorities = roles.stream()
//                    .map(role -> "ROLE_" + role)
//                    .map(SimpleGrantedAuthority::new)
//                    .toList();
//
//            return new UsernamePasswordAuthenticationToken(username, null, authorities);
//        });
//    }
}