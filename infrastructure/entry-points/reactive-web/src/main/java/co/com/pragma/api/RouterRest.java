package co.com.pragma.api;

import co.com.pragma.api.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    produces = {"application/json"},
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
                                    @ApiResponse(responseCode = "201", description = "Successful created user",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UserResponse.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                                    @ApiResponse(responseCode = "404", description = "Role not found"),
                                    @ApiResponse(responseCode = "409", description = "User already exists")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/user/dni/{dni}",
                    produces = {"application/json"},
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "getUserByDni",
                    operation = @Operation(
                            operationId = "GetUserByDni",
                            summary = "Get user by DNI",
                            parameters = {
                                    @Parameter(
                                            name = "dni",
                                            in = ParameterIn.PATH,
                                            required = true,
                                            description = "DNI of the user",
                                            schema = @Schema(type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Successful operation",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UserApiResponse.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "User not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "login",
                    operation = @Operation(
                            operationId = "LoginUser",
                            summary = "Authenticate user and generate JWT",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "User credentials for login",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = LoginRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Login successful",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = LoginResponse.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "401", description = "Invalid email or password"),
                                    @ApiResponse(responseCode = "400", description = "Invalid request body"),
                                    @ApiResponse(responseCode = "404", description = "User not found")
                            }
                    )
            )
    }
    )
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/users"), handler::createUser)
                .andRoute(GET("/api/v1/user/dni/{dni}"), handler::getUserByDni)
                .andRoute(POST("/api/v1/login"), handler::login);
    }
}
