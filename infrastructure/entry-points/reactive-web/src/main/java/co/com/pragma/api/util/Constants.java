package co.com.pragma.api.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String VALID_NAME = "El nombre no puede estar vacío o nulo";

    public static final String VALID_LAST_NAME = "El apellido no puede estar vacío o nulo";

    public static final String VALID_DOCUMENT = "El documento no puede estar vacío o nulo";

    public static final String VALID_EMAIL= "El correo no puede estar vacío o nulo";
    public static final String VALID_EMAIL_FORMAT = "Debe ser un correo válido";

    public static final String VALID_BASE_SALARY= "El salario base no puede ser nulo";
    public static final String VALID_BASE_SALARY_MIN = "El salario base debe ser al menos 0";
    public static final String VALID_BASE_SALARY_MAX = "El salario base no puede ser mayor a 15000000";

    public static final String BASE_SALARY_MIN = "0.0";
    public static final String BASE_SALARY_MAX = "15000000.0";
}
