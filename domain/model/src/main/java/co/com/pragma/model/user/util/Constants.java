package co.com.pragma.model.user.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class Constants {
    public static final String VALID_USER = "El usuario no puede ser nulo";

    public static final String VALID_NAME = "El nombre no puede estar vacío o nulo";

    public static final String VALID_LAST_NAME = "El apellido no puede estar vacío o nulo";

    public static final String VALID_DOCUMENT = "El documento no puede estar vacío o nulo";

    public static final String VALID_EMAIL= "El correo no puede estar vacío o nulo";
    public static final String VALID_EMAIL_FORMAT = "El correo no tiene un formato válido";
    public static final String VALID_EMAIL_DUPLICATE = "El correo electrónico ya está registrado por otro usuario.";

    public static final String VALID_BASE_SALARY= "El salario base no puede ser nulo";
    public static final String VALID_BASE_SALARY_MIN = "El salario base debe ser al menos 0";
    public static final String VALID_BASE_SALARY_MAX = "El salario base no puede ser mayor a 15000000";

    public static final BigDecimal BASE_SALARY_MIN = BigDecimal.valueOf(0.0);
    public static final BigDecimal BASE_SALARY_MAX = BigDecimal.valueOf(15000000.0);
}
