package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static co.com.pragma.api.util.Constants.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequest {

    @NotBlank(message = VALID_NAME)
    private String name;

    @NotBlank(message = VALID_LAST_NAME)
    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @NotBlank(message = VALID_DOCUMENT)
    private String nit;

    private String phone;

    @NotBlank(message = VALID_EMAIL)
    @Email(message = VALID_EMAIL_FORMAT)
    private String email;

    private String address;

    @NotNull(message = VALID_BASE_SALARY)
    @DecimalMin(value = BASE_SALARY_MIN,  message = VALID_BASE_SALARY_MIN)
    @DecimalMax(value = BASE_SALARY_MAX, message = VALID_BASE_SALARY_MAX)
    @JsonProperty("base_salary")
    private BigDecimal baseSalary;

}
