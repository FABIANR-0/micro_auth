package co.com.pragma.model.user.validation;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exception.DomainException;

import static co.com.pragma.model.user.util.Constants.*;

public class UserValidator {

    public void validate(User user) {
        if (user == null) {
            throw new DomainException(VALID_USER);
        }

        // Validar nombre
        if (user.getName() == null || user.getName().isBlank()) {
            throw new DomainException(VALID_NAME);
        }

        // Validar apellido
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new DomainException(VALID_LAST_NAME);
        }

        // Validar documento
        if (user.getNit() == null || user.getNit().isBlank()) {
            throw new DomainException(VALID_DOCUMENT);
        }

        // Validar correo
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new DomainException(VALID_EMAIL);
        }

        if (!user.getEmail().matches("^[^@]+@[^@]+\\.[a-zA-Z]{2,}$")) {
            throw new DomainException(VALID_EMAIL_FORMAT);
        }

        // Validar salario
        if (user.getBaseSalary() == null) {
            throw new DomainException(VALID_BASE_SALARY);
        }

        if (user.getBaseSalary().compareTo(BASE_SALARY_MIN) < 0) {
            throw new DomainException(VALID_BASE_SALARY_MIN);
        }

        if (user.getBaseSalary().compareTo(BASE_SALARY_MAX) > 0) {
            throw new DomainException(VALID_BASE_SALARY_MAX);
        }
    }
}
