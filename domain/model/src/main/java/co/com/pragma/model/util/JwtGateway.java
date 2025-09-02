package co.com.pragma.model.util;

import co.com.pragma.model.user.User;

public interface JwtGateway {

    String generateToken(User user, String role);

}
