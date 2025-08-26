package co.com.pragma.api;

import co.com.pragma.api.dto.UserRequest;
import co.com.pragma.api.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouterRestTest {

    @Mock
    private Handler handler;

    private WebTestClient webTestClient;
    private UserRequest userRequest;
    private UserResponse expectedResponse;

    @BeforeEach
    void setUp() {
        RouterRest routerRest = new RouterRest();

        RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(handler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();

        userRequest = UserRequest.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        expectedResponse = UserResponse.builder()
                .userId(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();
    }

    @Test
    @DisplayName("Should create user successfully and return 201")
    void shouldCreateUserSuccessfully() {

        when(handler.createUser(any()))
                .thenReturn(Mono.just(Objects.requireNonNull(ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(expectedResponse)
                        .block())));

        // When & Then
        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserResponse.class)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should return 409 when user already exists")
    void shouldReturnConflictWhenUserAlreadyExists() {

        when(handler.createUser(any()))
                .thenReturn(Mono.just(Objects.requireNonNull(ServerResponse.status(409)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("{\"error\": \"User already exists\"}")
                        .block())));

        // When & Then
        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }
}