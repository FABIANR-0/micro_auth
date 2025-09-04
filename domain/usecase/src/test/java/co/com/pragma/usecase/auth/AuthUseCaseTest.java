package co.com.pragma.usecase.auth;

import co.com.pragma.model.auth.Auth;
import co.com.pragma.model.auth.exception.InvalidCredentialsException;
import co.com.pragma.model.role.Role;
import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exception.ResourceNotFound;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.util.JwtGateway;
import co.com.pragma.model.util.LoggerGateway;
import co.com.pragma.model.util.PasswordGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private LoggerGateway log;

    @Mock
    private PasswordGateway passwordGateway;

    @Mock
    private JwtGateway jwtGateway;

    @InjectMocks
    private AuthUseCase authUseCase;

    private Auth auth;
    private User user;
    private Role role;
    private final String generatedToken = "jwt.token.generated";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        auth = Auth.builder()
                .email("juan.perez@example.com")
                .password("123456")
                .build();

        user = User.builder()
                .userId(1L)
                .name("Juan")
                .lastName("Pérez")
                .birthDate(LocalDate.of(1995, 5, 12))
                .dni("123456789")
                .phone("3001234567")
                .email("juan.perez@example.com")
                .address("Calle 45 #12-34")
                .baseSalary(new BigDecimal("2500000.00"))
                .password("encodedPassword")
                .roleId(1L)
                .build();

        role = Role.builder()
                .roleId(1L)
                .name("ADMIN")
                .description("Administrator role")
                .build();
    }

    @Test
    void login_Success() {
        // Arrange
        when(userRepository.getByEmail(auth.getEmail())).thenReturn(Mono.just(user));
        when(passwordGateway.matches(auth.getPassword(), user.getPassword())).thenReturn(Mono.just(true));
        when(roleRepository.findById(user.getRoleId())).thenReturn(Mono.just(role));
        when(jwtGateway.generateToken(user, role.getName())).thenReturn(generatedToken);

        // Act & Assert
        StepVerifier.create(authUseCase.login(auth))
                .expectNextMatches(result ->
                        result.getEmail().equals(auth.getEmail()) &&
                                result.getToken().equals(generatedToken))
                .verifyComplete();

        // Verify interactions
        verify(userRepository).getByEmail(auth.getEmail());
        verify(passwordGateway).matches(auth.getPassword(), user.getPassword());
        verify(roleRepository).findById(user.getRoleId());
        verify(jwtGateway).generateToken(user, role.getName());
        verify(log).info("Iniciando validación de credenciales");
        verify(log).info("Token generado exitosamente para el usuario: {}", auth.getEmail());
    }

    @Test
    void login_UserNotFound() {
        // Arrange
        when(userRepository.getByEmail(auth.getEmail())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(authUseCase.login(auth))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResourceNotFound &&
                                "usuario no existente".equals(throwable.getMessage()))
                .verify();

        // Verify interactions
        verify(userRepository).getByEmail(auth.getEmail());
        verify(passwordGateway, never()).matches(anyString(), anyString());
        verify(roleRepository, never()).findById(anyLong());
        verify(jwtGateway, never()).generateToken(any(User.class), anyString());
        verify(log).info("Iniciando validación de credenciales");
        verify(log).warn("Usuario no encontrado para el email: {}", auth.getEmail());
    }

    @Test
    void login_InvalidPassword() {
        // Arrange
        when(userRepository.getByEmail(auth.getEmail())).thenReturn(Mono.just(user));
        when(passwordGateway.matches(auth.getPassword(), user.getPassword())).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(authUseCase.login(auth))
                .expectErrorMatches(throwable ->
                        throwable instanceof InvalidCredentialsException &&
                                "Credenciales incorrectas".equals(throwable.getMessage()))
                .verify();

        // Verify interactions
        verify(userRepository).getByEmail(auth.getEmail());
        verify(passwordGateway).matches(auth.getPassword(), user.getPassword());
        verify(roleRepository, never()).findById(anyLong());
        verify(jwtGateway, never()).generateToken(any(User.class), anyString());
        verify(log).info("Iniciando validación de credenciales");
        verify(log).warn("Credenciales incorrectas para el usuario con email: {}", auth.getEmail());
    }

    @Test
    void login_RoleNotFound() {
        // Arrange
        when(userRepository.getByEmail(auth.getEmail())).thenReturn(Mono.just(user));
        when(passwordGateway.matches(auth.getPassword(), user.getPassword())).thenReturn(Mono.just(true));
        when(roleRepository.findById(user.getRoleId())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(authUseCase.login(auth))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResourceNotFound &&
                                "Rol no encontrado para el usuario".equals(throwable.getMessage()))
                .verify();

        // Verify interactions
        verify(userRepository).getByEmail(auth.getEmail());
        verify(passwordGateway).matches(auth.getPassword(), user.getPassword());
        verify(roleRepository).findById(user.getRoleId());
        verify(jwtGateway, never()).generateToken(any(User.class), anyString());
        verify(log).info("Iniciando validación de credenciales");
    }

    @Test
    void login_PasswordGatewayError() {
        // Arrange
        RuntimeException passwordException = new RuntimeException("Password verification failed");
        when(userRepository.getByEmail(auth.getEmail())).thenReturn(Mono.just(user));
        when(passwordGateway.matches(auth.getPassword(), user.getPassword()))
                .thenReturn(Mono.error(passwordException));

        // Act & Assert
        StepVerifier.create(authUseCase.login(auth))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                "Password verification failed".equals(throwable.getMessage()))
                .verify();

        // Verify interactions
        verify(userRepository).getByEmail(auth.getEmail());
        verify(passwordGateway).matches(auth.getPassword(), user.getPassword());
        verify(roleRepository, never()).findById(anyLong());
        verify(jwtGateway, never()).generateToken(any(User.class), anyString());
        verify(log).info("Iniciando validación de credenciales");
    }

}