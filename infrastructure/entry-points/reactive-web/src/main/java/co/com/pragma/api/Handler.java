package co.com.pragma.api;

import co.com.pragma.api.dto.LoginRequest;
import co.com.pragma.api.dto.UserRequest;
import co.com.pragma.api.mapper.UserMapper;
import co.com.pragma.api.util.ValidationUtils;
import co.com.pragma.usecase.auth.AuthUseCase;
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

    private final AuthUseCase authUseCase;

    private final ValidationUtils validationUtils;

    public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
        return validationUtils.validateBody(serverRequest, UserRequest.class)
                .flatMap(userRequest -> userUseCase.createUser(userMapper.toUser(userRequest))
                        .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(userMapper.toDto(user)))

                );
    }

    public Mono<ServerResponse> getUserByDni(ServerRequest serverRequest) {
        String dni = serverRequest.pathVariable("dni");
        return userUseCase.getClientByDni(dni)
                .flatMap(user -> ServerResponse.ok().bodyValue(userMapper.toDtoApi(user)));
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return validationUtils.validateBody(serverRequest, LoginRequest.class)
                .flatMap(loginRequest -> authUseCase.login(userMapper.toAuthLogin(loginRequest))
                        .flatMap(auth -> ServerResponse.status(HttpStatus.OK).bodyValue(userMapper.toDtoLogin(auth)))

                );
    }
}
