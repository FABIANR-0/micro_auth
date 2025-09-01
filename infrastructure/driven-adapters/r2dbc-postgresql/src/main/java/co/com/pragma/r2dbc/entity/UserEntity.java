package co.com.pragma.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("\"user\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @Column("user_id")
    private Long userId;

    private String name;

    @Column("last_name")
    private String lastName;

    @Column("birth_date")
    private LocalDate birthDate;

    private String dni;

    private String phone;

    private String email;

    private String address;

    @Column("base_salary")
    private BigDecimal baseSalary;

    @Column("role_id")
    private Long roleId;

    private String password;
}