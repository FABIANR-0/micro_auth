package co.com.pragma.usecase.user;

import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exception.ConflictException;
import co.com.pragma.model.user.exception.ResourceNotFound;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.util.PasswordGateway;
import co.com.pragma.model.util.TransactionalGateway;
import co.com.pragma.model.util.LoggerGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static co.com.pragma.model.user.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private LoggerGateway log;

    @Mock
    private PasswordGateway passwordGateway;

    @Mock
    private TransactionalGateway transactional;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
                .password("123456")
                .build();

        when(transactional.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.existsById(user.getRoleId())).thenReturn(Mono.just(true));
        String encodedPassword = "encoded123456";
        when(passwordGateway.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.create(any(User.class))).thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(userUseCase.createUser(user))
                .expectNextMatches(createdUser -> createdUser.getEmail().equals(user.getEmail()))
                .verifyComplete();

        // Verify interactions
        verify(userRepository).existsByEmail(user.getEmail());
        verify(roleRepository).existsById(user.getRoleId());
        verify(passwordGateway).encode("123456");
        verify(userRepository).create(user);

        // Verify that the password was encoded before saving
        assert user.getPassword().equals(encodedPassword);
    }

    @Test
    void createUser_EmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(userUseCase.createUser(user))
                .expectErrorMatches(throwable ->
                        throwable instanceof ConflictException &&
                                VALID_EMAIL_DUPLICATE.equals(throwable.getMessage()))
                .verify();

        // Verify interactions
        verify(userRepository).existsByEmail(user.getEmail());
        verify(roleRepository, never()).existsById(anyLong());
        verify(userRepository, never()).create(any());
    }

    @Test
    void createUser_RoleDoesNotExist() {
        // Arrange
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.existsById(user.getRoleId())).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(userUseCase.createUser(user))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResourceNotFound &&
                                VALID_ROLE_EXISTS.equals(throwable.getMessage()))
                .verify();

        // Verify interactions
        verify(userRepository).existsByEmail(user.getEmail());
        verify(roleRepository).existsById(user.getRoleId());
        verify(userRepository, never()).create(any());
    }

    @Test
    void getClientByDni_UserExists() {
        // Arrange
        String dni = "123456789";
        when(userRepository.getByDni(dni)).thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(userUseCase.getClientByDni(dni))
                .expectNextMatches(foundUser -> foundUser.getDni().equals(dni))
                .verifyComplete();

        // Verify interactions
        verify(userRepository).getByDni(dni);
    }

    @Test
    void getClientByDni_UserNotFound() {
        // Arrange
        String dni = "987654321";
        when(userRepository.getByDni(dni)).thenReturn(Mono.empty());

        String validUserExists = VALID_USER_EXISTS + dni;
        // Act & Assert
        StepVerifier.create(userUseCase.getClientByDni(dni))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResourceNotFound &&
                                validUserExists.equals(throwable.getMessage()))
                .verify();

        // Verify interactions
        verify(userRepository).getByDni(dni);
    }
}
