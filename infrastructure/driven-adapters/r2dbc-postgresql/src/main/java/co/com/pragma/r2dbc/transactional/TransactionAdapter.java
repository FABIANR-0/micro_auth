package co.com.pragma.r2dbc.transactional;

import co.com.pragma.model.util.TransactionalGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionAdapter implements TransactionalGateway {

    private final TransactionalOperator transactionalOperator;

    @Override
    public <T> Mono<T> executeInTransaction(Mono<T> action) {
        return transactionalOperator.transactional(action);
    }
}
