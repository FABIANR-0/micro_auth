package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponse {

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("birth_date")
    private LocalDate birthDate;

    private String nit;

    private String phone;

    private String email;

    private String address;

    @JsonProperty("role_id")
    private Long roleId;

    @JsonProperty("base_salary")
    private BigDecimal baseSalary;
}
