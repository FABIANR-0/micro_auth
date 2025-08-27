package co.com.pragma.usecase.user;

import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exception.ResourceNotFound;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.user.exception.ConflictException;
import co.com.pragma.model.user.util.UserCaseLogger;
import reactor.core.publisher.Mono;

import static co.com.pragma.model.user.util.Constants.VALID_EMAIL_DUPLICATE;
import static co.com.pragma.model.user.util.Constants.VALID_ROLE_EXISTS;

public class UserUseCase {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserCaseLogger log;

    public UserUseCase(UserRepository userRepository, RoleRepository roleRepository, UserCaseLogger log) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.log = log;
    }

    public Mono<User> createUser(User user) {

        log.trace("Nuevo usuario a registrar con cc: {}", user.getNit());

        return userRepository.existsByEmail(user.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        log.error("El email {} ya está registrado", user.getEmail());
                        return Mono.error(new ConflictException(VALID_EMAIL_DUPLICATE));
                    }
                    return roleRepository.existsById(user.getRoleId());
                })
                .flatMap(roleExists -> {
                    if (!roleExists) {
                        log.error("El rol con id {} no existe ", user.getRoleId());
                        return Mono.error(new ResourceNotFound(VALID_ROLE_EXISTS));
                    }
                    return userRepository.create(user)
                            .doOnSuccess(createdUser -> log.info("Usuario creado con id: {}", createdUser.getUserId()));
                });
    }
}
