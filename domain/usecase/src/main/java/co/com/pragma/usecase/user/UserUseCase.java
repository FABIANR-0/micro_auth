package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.user.exception.ConflictException;
import co.com.pragma.model.user.validation.UserValidator;
import reactor.core.publisher.Mono;

import static co.com.pragma.model.user.util.Constants.VALID_EMAIL_DUPLICATE;

public class UserUseCase {

    private final UserRepository userRepository;

    private final UserValidator userValidator;

    public UserUseCase(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public Mono<User> createUser(User user) {
        return Mono.fromCallable(() -> {
                    userValidator.validate(user);
                    return user;
                })
                .flatMap(usr -> userRepository.existsByEmail(usr.getEmail())
                        .filter(exists -> !exists)
                        .switchIfEmpty(Mono.error(new ConflictException(VALID_EMAIL_DUPLICATE)))
                        .flatMap(exists -> userRepository.create(usr))
                );
    }

}
