package co.cuencas.usersadmin.security.controller;

import co.cuencas.usersadmin.dto.ResponseMessageDto;
import co.cuencas.usersadmin.security.dto.SignInDto;
import co.cuencas.usersadmin.security.dto.SignUpDto;
import co.cuencas.usersadmin.security.entity.UserApp;
import co.cuencas.usersadmin.security.service.UseCase.UserService;
import co.cuencas.usersadmin.security.util.AuthenticationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<ResponseMessageDto> signUpUser(@RequestBody @Valid SignUpDto signUpDto) {
        userService.signUpUser(signUpDto);
        return new ResponseEntity<>(new ResponseMessageDto("Usuario registrado con exito"),
                                    HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<ResponseMessageDto> signInUser(@RequestBody @Valid SignInDto signInDto) {
        String accessToken = userService.signInUser(signInDto);

        // Search for the user in the database and build the user details
        UserDetails userDetails = userService.loadUserByUsername(signInDto.getIdentification());
        // Authenticate the user within the Spring Security context
        AuthenticationUtil.authenticateUserInSecurityContext(userDetails);

        return new ResponseEntity<>(new ResponseMessageDto(accessToken),
                                    HttpStatus.OK);

    }

    /**
     * Get the authenticated user details
     *
     * @return user
     */
    @GetMapping("/user-details")
    public Optional<UserApp> getUserDetails() {
        // Get the user who is logged in
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                                                                     .getAuthentication()
                                                                     .getPrincipal();
        // Get the identification
        String identification = userDetails.getUsername();
        // Get the user by the identification
        return userService.getUserByIdentification(identification);
    }
}
