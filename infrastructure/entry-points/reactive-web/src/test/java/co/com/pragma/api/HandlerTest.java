package co.com.pragma.api;

import co.com.pragma.api.dto.UserRequest;
import co.com.pragma.api.dto.UserResponse;
import co.com.pragma.api.mapper.UserMapper;
import co.com.pragma.api.util.ValidationUtils;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HandlerTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private ValidationUtils validationUtils;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private Handler handler;

    @Test
    void createUser_ShouldReturn201Created_WhenUserIsCreatedSuccessfully() {
        // Given
        UserRequest userRequest = UserRequest.builder()
                .name("Fabian")
                .lastName("Rincon")
                .birthDate(LocalDate.of(1995, 8, 26))
                .dni("123456789")
                .phone("3001234567")
                .email("fabian.rincon@example.com")
                .address("Calle 123 #45-67")
                .roleId(1L)
                .baseSalary(BigDecimal.valueOf(2500000))
                .build();

        UserResponse userResponse = UserResponse.builder()
                .userId(1L)
                .name("Fabian")
                .lastName("Rincon")
                .birthDate(LocalDate.of(1995, 8, 26))
                .dni("123456789")
                .phone("3001234567")
                .email("fabian.rincon@example.com")
                .address("Calle 123 #45-67")
                .roleId(1L)
                .baseSalary(BigDecimal.valueOf(2500000))
                .build();

        User user = User.builder()
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


        // When
        when(validationUtils.validateBody(eq(serverRequest), eq(UserRequest.class)))
                .thenReturn(Mono.just(userRequest));
        when(userMapper.toUser(userRequest)).thenReturn(user);
        when(userUseCase.createUser(user)).thenReturn(Mono.just(user));
        when(userMapper.toDto(user)).thenReturn(userResponse);

        // Then
        StepVerifier.create(handler.createUser(serverRequest))
                .expectNextMatches(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
                    return true;
                })
                .verifyComplete();
    }
}
