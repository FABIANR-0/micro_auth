package co.com.pragma.api.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter implements WebFilter {

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.trace("Request: {} URI:{}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
        return chain.filter(exchange)
                .doFinally(signalType ->
                        log.trace("Response status: {}", exchange.getResponse().getStatusCode())
                );
    }
}