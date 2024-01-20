package co.cuencas.usersadmin.security.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInDto {
    @NotEmpty(message = "La identificación es obligatoria")
    private String identification;

    @NotEmpty(message = "La contraseña es obligatoria")
    private String password;
}
