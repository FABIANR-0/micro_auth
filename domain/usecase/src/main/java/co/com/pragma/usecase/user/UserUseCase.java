package co.com.pragma.usecase.user;

import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exception.DomainException;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.user.exception.ConflictException;
import reactor.core.publisher.Mono;

import static co.com.pragma.model.user.util.Constants.VALID_EMAIL_DUPLICATE;
import static co.com.pragma.model.user.util.Constants.VALID_ROLE_EXISTS;

public class UserUseCase {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    public UserUseCase(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Mono<User> createUser(User user) {

        System.out.println("[INFO] Nuevo usuario a registrar con cc: " + user.getNit());

        return userRepository.existsByEmail(user.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        System.out.println("[ERROR] El email ya está registrado: " + user.getEmail());
                        return Mono.error(new ConflictException(VALID_EMAIL_DUPLICATE));
                    }
                    return roleRepository.existsById(user.getRoleId());
                })
                .flatMap(roleExists -> {
                    if (!roleExists) {
                        System.out.println("[ERROR] El rol no existe con id: " + user.getRoleId());
                        return Mono.error(new DomainException(VALID_ROLE_EXISTS));
                    }
                    return userRepository.create(user)
                            .doOnSuccess(createdUser -> System.out.println("[SUCCESS] Usuario creado con id: " + createdUser.getUserId()));
                });
    }
}
