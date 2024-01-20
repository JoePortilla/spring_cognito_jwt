package co.cuencas.usersadmin.security.service;

import co.cuencas.usersadmin.security.dto.SignInDto;
import co.cuencas.usersadmin.security.dto.SignUpDto;
import co.cuencas.usersadmin.security.entity.UserApp;

import java.util.Optional;

public interface UserService {
    void createUser(UserApp user);

    void signUpUser(SignUpDto signUpDto);

    String signInUser(SignInDto signInDto);

    Optional<UserApp> findUserByIdentification(String userIdentification);

}
