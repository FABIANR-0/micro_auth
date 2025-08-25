package co.com.pragma.api.util;

import co.com.pragma.api.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class ValidationUtils {

    private final SmartValidator validator;

    public <T> Mono<T> validateBody(ServerRequest request, Class<T> clazz) {
        return request.bodyToMono(clazz)
                .flatMap(body -> {
                            var errors = new BeanPropertyBindingResult(body, clazz.getName());
                            validator.validate(body, errors);

                            if (errors.hasErrors()) {
                                return Mono.error(new ValidationException(errors));
                            }

                            return Mono.just(body);
                        }
                );
    }
}