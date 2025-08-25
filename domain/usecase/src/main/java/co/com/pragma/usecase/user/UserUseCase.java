package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> createUser(User user) {
        return userRepository.existsByEmail(user.getEmail())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new ConflictException("El correo ya esta registrado con otro usuario")))
                .flatMap(exists -> userRepository.create(user));
        //userRepository.create(user);
    }

}
