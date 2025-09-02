package co.com.pragma.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static co.com.pragma.api.util.Constants.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginRequest {

    @NotBlank(message = VALID_EMAIL)
    private String email;

    @NotBlank(message = VALID_PASSWORD)
    private String password;
}
