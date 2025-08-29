package co.com.pragma.usecase.user;

import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exception.ResourceNotFound;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.user.exception.ConflictException;
import co.com.pragma.model.util.TransactionalGateway;
import co.com.pragma.model.util.LoggerGateway;
import reactor.core.publisher.Mono;

import static co.com.pragma.model.user.util.Constants.*;

public class UserUseCase {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final LoggerGateway log;

    private final TransactionalGateway transactional;

    public UserUseCase(UserRepository userRepository, RoleRepository roleRepository, LoggerGateway log, TransactionalGateway transactional) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.log = log;
        this.transactional = transactional;
    }

    public Mono<User> createUser(User user) {

        log.trace("Nuevo usuario a registrar con cc: {}", user.getDni());

        // El caso de uso queda en una transacción
        return transactional.executeInTransaction(
                userRepository.existsByEmail(user.getEmail())
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
                        })
        );
    }

    public Mono<User> getClientByDni(String dni) {
        return userRepository.getByDni(dni)
                .switchIfEmpty(Mono.error(new ResourceNotFound(VALID_USER_EXISTS + dni)));
    }
}
