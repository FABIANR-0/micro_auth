package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private  User user;

    private  UserEntity userEntity;

    @BeforeEach
    void setUp() {
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
                .build();

        userEntity = UserEntity.builder()
                .userId(1L)
                .name("Carlos")
                .lastName("Ramírez")
                .birthDate(LocalDate.of(1990, 3, 15))
                .dni("123456789")
                .phone("3106547890")
                .email("carlos.ramirez@example.com")
                .address("Carrera 10 #20-30")
                .baseSalary(new BigDecimal("3500000.00"))
                .build();
    }

    @Test
    void shouldFindUserById() {
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        when(repository.findById(1L)).thenReturn(Mono.just(userEntity));

        Mono<User> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNextMatches(u -> u.getUserId().equals(1L) && u.getName().equals("Juan"))
                .verifyComplete();
    }

    @Test
    void showFindAllUsers() {
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        when(repository.findAll()).thenReturn(Flux.just(userEntity));

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        when(repository.existsByEmailIgnoreCase("juan.perez@example.com")).thenReturn(Mono.just(true));

        Mono<Boolean> result = repositoryAdapter.existsByEmail("juan.perez@example.com");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        when(repository.existsByEmailIgnoreCase("unknown@example.com")).thenReturn(Mono.just(false));

        Mono<Boolean> result = repositoryAdapter.existsByEmail("unknown@example.com");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(mapper.map(userEntity, User.class)).thenReturn(user);
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);

        when(repository.save(userEntity)).thenReturn(Mono.just(userEntity));

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(repository.save(userEntity)).thenReturn(Mono.just(userEntity));
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.create(user);

        StepVerifier.create(result)
                .expectNextMatches(createdUser ->
                        createdUser.getUserId().equals(user.getUserId()) &&
                                createdUser.getDni().equals(user.getDni())
                ).verifyComplete();

        verify(repository).save(any(UserEntity.class));
    }
}
