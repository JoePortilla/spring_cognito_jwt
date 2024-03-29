package co.cuencas.usersadmin.controller;

import co.cuencas.usersadmin.dto.ResponseMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleTestingController {
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseMessageDto> userMessage() {
        return new ResponseEntity<>(new ResponseMessageDto("El usuario tiene el rol usuario"), HttpStatus.OK);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessageDto> adminMessage() {
        return new ResponseEntity<>(new ResponseMessageDto("El usuario tiene el rol admin"), HttpStatus.OK);
    }
}