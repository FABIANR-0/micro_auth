package co.com.pragma.api;

import co.com.pragma.api.dto.UserRequest;
import co.com.pragma.api.mapper.UserMapper;
import co.com.pragma.api.util.ValidationUtils;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserMapper userMapper;

    private final UserUseCase userUseCase;

    private final ValidationUtils validationUtils;

    public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
        return validationUtils.validateBody(serverRequest, UserRequest.class)
                .flatMap(userRequest -> userUseCase.createUser(userMapper.toUser(userRequest))
                        .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(userMapper.toDto(user)))

                );
    }
}
