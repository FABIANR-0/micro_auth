package co.com.pragma.usecase.user.exception;
import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
