package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static co.com.pragma.api.util.Constants.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserRequest {

    @NotBlank(message = VALID_NAME)
    @Size(min = 3, max = 50, message = VALID_NAME_SIZE)
    private String name;

    @NotBlank(message = VALID_LAST_NAME)
    @Size(min = 3, max = 50, message = VALID_LAST_NAME_SIZE)
    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    @NotBlank(message = VALID_DOCUMENT)
    @Size(max = 20, message = VALID_DOCUMENT_SIZE)
    private String dni;

    @Size(max = 20, message = VALID_PHONE_SIZE)
    private String phone;

    @NotBlank(message = VALID_EMAIL)
    @Email(message = VALID_EMAIL_FORMAT)
    @Size(min = 1, max = 100, message = VALID_EMAIL_SIZE)
    private String email;

    @Size(max = 100, message = VALID_ADDRESS_SIZE)
    private String address;

    @NotNull(message = VALID_ROLE)
    @Positive(message = VALID_ROLE_NUMBER)
    @JsonProperty("role_id")
    private Long roleId;

    @NotNull(message = VALID_BASE_SALARY)
    @DecimalMin(value = BASE_SALARY_MIN,  message = VALID_BASE_SALARY_MIN)
    @DecimalMax(value = BASE_SALARY_MAX, message = VALID_BASE_SALARY_MAX)
    @JsonProperty("base_salary")
    private BigDecimal baseSalary;

    @NotBlank(message = VALID_PASSWORD)
    String password;

}
