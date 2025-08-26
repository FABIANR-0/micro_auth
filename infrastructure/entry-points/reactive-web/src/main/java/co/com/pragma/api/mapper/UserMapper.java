package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UserRequest;
import co.com.pragma.api.dto.UserResponse;
import co.com.pragma.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest dto);

    UserResponse toDto(User user);
}
