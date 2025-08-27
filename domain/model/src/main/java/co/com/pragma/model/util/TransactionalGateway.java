package co.com.pragma.model.util;

import reactor.core.publisher.Mono;

public interface TransactionalGateway {
    <T> Mono<T> executeInTransaction(Mono<T> action);
}
