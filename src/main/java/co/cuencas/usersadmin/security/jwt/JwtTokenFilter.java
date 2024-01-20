package co.cuencas.usersadmin.security.jwt;


import co.cuencas.usersadmin.security.service.UseCase.UserService;
import co.cuencas.usersadmin.security.util.AuthenticationUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter through which each of the requests made by the client will pass.
 * This filter verifies that the token is not null and is valid, if it is checked, the client proceeds to obtain the
 * resources of the request.
 */
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Get the token of the Authorization Header of the request
            String token = request.getHeader("Authorization");
            // Validate that the token is not null and is valid.
            if (token != null && jwtProvider.validateToken(token)) {
                // Get the username (i.e.: identification) of the token
                String identification = jwtProvider.getUserNameFromToken(token);
                // Search for the user in the database and build the user details
                UserDetails userDetails = userService.loadUserByUsername(identification);
                // Authenticate the user within the Spring Security context
                AuthenticationUtil.authenticateUserInSecurityContext(userDetails);
            }
        }
        catch (Exception e) {
            // TODO mejorar la exception
            System.out.println(e.getMessage());
            // SecurityContextHolder.clearContext();
            // throw e;
        }
        filterChain.doFilter(request, response);
    }

}
