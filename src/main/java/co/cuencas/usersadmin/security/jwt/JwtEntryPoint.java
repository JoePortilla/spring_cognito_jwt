package co.cuencas.usersadmin.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The token will be verified to check whether the user making the request is authorized or not authorized to perform
 * the process.
 */
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        // TODO: Send exception
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                           "Usuario no autorizado");
    }
}

