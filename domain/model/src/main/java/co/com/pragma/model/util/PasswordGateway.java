package co.com.pragma.model.util;

import reactor.core.publisher.Mono;

public interface PasswordGateway {
    String encode(String password);
    Mono<Boolean> matches(String rawPassword, String encodedPassword);
}
