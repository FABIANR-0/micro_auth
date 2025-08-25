package co.com.pragma.api;

import co.com.pragma.api.dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.pragma.api.config.UserPath;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class RouterRest {

    private final UserPath userPath;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/usecase/path",
                    produces = { "application/json" },
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGETUseCase",
                    operation = @Operation(
                            operationId = "getUseCase",
                            summary = "Get UseCase",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/usecase/otherpath",
                    produces = { "application/json" },
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenPOSTUseCase",
                    operation = @Operation(
                            operationId = "postUseCase",
                            summary = "Post UseCase",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users",
                    produces = { "application/json" },
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "createUser",
                    operation = @Operation(
                            operationId = "CreateNewUser",
                            summary = "Create new user client",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "User object to create",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UserRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/usecase/path"), handler::listenGETUseCase)
                .andRoute(POST("/api/usecase/otherpath"), handler::listenPOSTUseCase)
                .and(route(POST(userPath.users()), handler::createUser));
    }
}
