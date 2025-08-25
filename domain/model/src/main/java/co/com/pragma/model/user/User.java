package co.com.pragma.model.user;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private Long userId;

    private String name;

    private String lastName;

    private LocalDate birthDate;

    private String nit;

    private String phone;

    private String email;

    private String address;

    private BigDecimal baseSalary;
}
