package co.com.pragma.model.util;

public interface PasswordGateway {
    String encode(String password);
    boolean matches(String rawPassword, String encodedPassword);
}
