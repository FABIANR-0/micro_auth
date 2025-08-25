package co.com.pragma.model.user.exception;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
