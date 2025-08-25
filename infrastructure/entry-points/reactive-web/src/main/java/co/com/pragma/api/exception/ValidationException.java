package co.com.pragma.api.exception;
import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class ValidationException extends RuntimeException {

    private final Errors errors;

    public ValidationException(Errors errors) {
        super("Error de validación");
        this.errors = errors;
    }
}
