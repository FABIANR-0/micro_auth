package co.com.pragma.usecase.auth;

import co.com.pragma.model.auth.Auth;
import co.com.pragma.model.auth.exception.InvalidCredentialsException;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.exception.ResourceNotFound;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.util.JwtGateway;
import co.com.pragma.model.util.LoggerGateway;
import co.com.pragma.model.util.PasswordGateway;

import reactor.core.publisher.Mono;

public class AuthUseCase {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final LoggerGateway log;

    private final PasswordGateway password;

    private final JwtGateway jwtGateway;

    public AuthUseCase(UserRepository userRepository, LoggerGateway log, PasswordGateway password, RoleRepository roleRepository, JwtGateway jwtGateway) {
        this.userRepository = userRepository;
        this.log = log;
        this.password = password;
        this.roleRepository = roleRepository;
        this.jwtGateway = jwtGateway;
    }

    public Mono<Auth> login(Auth auth) {
        log.info("Iniciando validación de credenciales");
        return userRepository.getByEmail(auth.getEmail())
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Usuario no encontrado para el email: {}", auth.getEmail());
                    return Mono.error(new ResourceNotFound("usuario no existente"));
                }))
                .flatMap(user -> password.matches(auth.getPassword(), user.getPassword())
                        .flatMap(match -> {
                            if (!match) {
                                log.warn("Credenciales incorrectas para el usuario con email: {}", auth.getEmail());
                                return Mono.error(new InvalidCredentialsException("Credenciales incorrectas"));
                            }
                            return roleRepository.findById(user.getRoleId())
                                    .switchIfEmpty(Mono.error(new ResourceNotFound("Rol no encontrado para el usuario")))
                                    .map(role -> {
                                        String token = jwtGateway.generateToken(user, role.getName());
                                        auth.setToken(token);
                                        log.info("Token generado exitosamente para el usuario: {}", auth.getEmail());
                                        return auth;
                                    });
                        })
                );
    }
}
