package co.cuencas.usersadmin.security.jwt;


import co.cuencas.usersadmin.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro por donde pasará cada petición que se realice del lado del cliente
 * este filtro verificar que el token no sea nulo y sea válido, si se comprueba el cliente obtiene los recursos de la
 * petición
 */
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

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
                // Search for the user in the database and Save it in the spring security context
                UserDetails userDetails = userDetailsService.loadUserByUsername(identification);
                // Authentication with spring security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                                                null,
                                                                userDetails.getAuthorities());
                // Save the Authentication in the spring security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
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
