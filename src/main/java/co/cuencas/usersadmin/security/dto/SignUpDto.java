package co.cuencas.usersadmin.security.dto;

import co.cuencas.usersadmin.security.entity.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class SignUpDto {
    @NotEmpty(message = "La identificación es obligatoria")
    private String identification;

    @NotEmpty(message = "La contraseña es obligatoria")
    private String password;

    @NotEmpty(message = "El nombre completo es obligatorio")
    private String fullName;

    private String phone;

    @NotEmpty(message = "El correo electrónico es obligatorio")
    private String email;

    private Timestamp createdDate;

    private String organization;

    private UserRole userRole;
}
