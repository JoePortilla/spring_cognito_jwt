package co.cuencas.usersadmin.security.service.UseCase;

import co.cuencas.usersadmin.security.dto.SignInDto;
import co.cuencas.usersadmin.security.dto.SignUpDto;
import co.cuencas.usersadmin.security.entity.UserApp;

import java.util.Optional;

public interface UserService {

    void signUpUser(SignUpDto signUpDto);

    String signInUser(SignInDto signInDto);

    Optional<UserApp> getUserByIdentification(String userIdentification);

}
