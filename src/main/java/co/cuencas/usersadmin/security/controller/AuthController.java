package co.cuencas.usersadmin.security.controller;

import co.cuencas.usersadmin.dto.ResponseMessageDto;
import co.cuencas.usersadmin.security.dto.SignInDto;
import co.cuencas.usersadmin.security.dto.SignUpDto;
import co.cuencas.usersadmin.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<ResponseMessageDto> signUpUser(@RequestBody @Valid SignUpDto signUpDto,
                                                         BindingResult bindingResult) {
        // A traves de binding result se muestra el mensaje por defecto si los atributos tienen errores, que se
        // configuro en @NotEmpty(message = "La identificación es obligatoria")
        if (bindingResult.hasFieldErrors()) {
            return new ResponseEntity<>(new ResponseMessageDto(bindingResult.getFieldError().getDefaultMessage()),
                                        HttpStatus.BAD_REQUEST);
        }
        userService.signUpUser(signUpDto);
        return new ResponseEntity<>(new ResponseMessageDto("Usuario registrado con exito"),
                                    HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<ResponseMessageDto> signInUser(@RequestBody @Valid SignInDto signInDto,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new ResponseEntity<>(new ResponseMessageDto(bindingResult.getFieldError().getDefaultMessage()),
                                        HttpStatus.BAD_REQUEST);
        }

        String accessToken = userService.signInUser(signInDto);

        // // Search for the user in the database and Save it in the spring security context
        // UserDetails userDetails = userDetailsService.loadUserByUsername(signInDto.getEmail());
        //
        // // Authentication with spring security
        // UsernamePasswordAuthenticationToken authentication =
        //         new UsernamePasswordAuthenticationToken(userDetails,
        //                                                 null,
        //                                                 userDetails.getAuthorities());
        // // Save the Authentication in the spring security context
        // SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(new ResponseMessageDto(accessToken),
                                    HttpStatus.OK);

    }
}
