package co.com.pragma.api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserApiResponse {

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    private String dni;

    private String email;

    @JsonProperty("base_salary")
    private BigDecimal baseSalary;
}
