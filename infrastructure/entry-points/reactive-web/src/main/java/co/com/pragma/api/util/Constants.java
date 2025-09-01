package co.com.pragma.api.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String VALID_NAME = "El nombre es un campo requerido.";
    public static final String VALID_NAME_SIZE = "El nombre debe tener entre 3 y 50 caracteres.";

    public static final String VALID_LAST_NAME = "El apellido es un campo requerido.";
    public static final String VALID_LAST_NAME_SIZE = "El apellido debe tener entre 3 y 50 caracteres.";

    public static final String VALID_DOCUMENT = "El número de documento es un campo requerido.";
    public static final String VALID_DOCUMENT_SIZE = "El número de documento no puede tener más de 20 caracteres.";

    public static final String VALID_ROLE = "El rol es un campo requerido.";
    public static final String VALID_ROLE_NUMBER = "El rol actual no es válido.";

    public static final String VALID_PHONE_SIZE = "El número de teléfono no puede tener más de 20 caracteres.";

    public static final String VALID_EMAIL = "El correo electrónico es un campo requerido.";
    public static final String VALID_EMAIL_FORMAT = "El correo electrónico no tiene un formato válido.";
    public static final String VALID_EMAIL_SIZE = "El correo electrónico no puede tener más de 100 caracteres.";

    public static final String VALID_ADDRESS_SIZE = "La dirección no puede tener más de 100 caracteres.";

    public static final String VALID_BASE_SALARY = "El salario base es un campo requerido.";
    public static final String VALID_BASE_SALARY_MIN = "El salario base no puede ser menor a 0.";
    public static final String VALID_BASE_SALARY_MAX = "El salario base no puede superar los 15.000.000.";

    public static final String BASE_SALARY_MIN = "0.0";
    public static final String BASE_SALARY_MAX = "15000000.0";

    public static final String PASSWORD_VALID = "La contraseña es un campo requerido.";
}
