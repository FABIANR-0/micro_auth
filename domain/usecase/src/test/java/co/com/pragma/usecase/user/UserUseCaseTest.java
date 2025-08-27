package co.com.pragma.usecase.user;

import co.com.pragma.model.role.gateways.RoleRepository;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exception.ConflictException;
import co.com.pragma.model.user.exception.ResourceNotFound;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.user.util.UserCaseLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static co.com.pragma.model.user.util.Constants.VALID_EMAIL_DUPLICATE;
import static co.com.pragma.model.user.util.Constants.VALID_ROLE_EXISTS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserCaseLogger log;

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
                .nit("123456789")
                .phone("3001234567")
                .email("juan.perez@example.com")
                .address("Calle 45 #12-34")
                .baseSalary(new BigDecimal("2500000.00"))
                .build();
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.existsById(user.getRoleId())).thenReturn(Mono.just(true));
        when(userRepository.create(any(User.class))).thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(userUseCase.createUser(user))
                .expectNextMatches(createdUser -> createdUser.getEmail().equals(user.getEmail()))
                .verifyComplete();

        // Verify interactions
        verify(userRepository).existsByEmail(user.getEmail());
        verify(roleRepository).existsById(user.getRoleId());
        verify(userRepository).create(user);
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
}
