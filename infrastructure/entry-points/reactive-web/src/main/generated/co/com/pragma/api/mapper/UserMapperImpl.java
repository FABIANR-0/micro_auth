package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UserRequest;
import co.com.pragma.model.user.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-24T16:51:17-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.2 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserRequest dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( dto.getName() );
        user.lastName( dto.getLastName() );
        user.birthDate( dto.getBirthDate() );
        user.nit( dto.getNit() );
        user.phone( dto.getPhone() );
        user.email( dto.getEmail() );
        user.address( dto.getAddress() );
        user.baseSalary( dto.getBaseSalary() );

        return user.build();
    }

    @Override
    public UserRequest toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserRequest userRequest = new UserRequest();

        userRequest.setName( user.getName() );
        userRequest.setLastName( user.getLastName() );
        userRequest.setBirthDate( user.getBirthDate() );
        userRequest.setNit( user.getNit() );
        userRequest.setPhone( user.getPhone() );
        userRequest.setEmail( user.getEmail() );
        userRequest.setAddress( user.getAddress() );
        userRequest.setBaseSalary( user.getBaseSalary() );

        return userRequest;
    }
}
