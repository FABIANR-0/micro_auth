package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.*;
import co.com.pragma.model.auth.Auth;
import co.com.pragma.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest dto);

    UserResponse toDto(User user);

    UserApiResponse toDtoApi(User user);

    Auth toAuthLogin(LoginRequest dto);

    LoginResponse toDtoLogin(Auth auth);
}
