package co.cuencas.usersadmin.security.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class to authenticate a user within the Spring Security context.
 * This class provides a method to set the authentication token in SecurityContextHolder.
 */
public class AuthenticationUtil {

    /**
     * Authenticates a user within the Spring Security context.
     *
     * @param userDetails User Details representing the authenticated user.
     *                    It includes information such as the user's name and authorities.
     */
    public static void authenticateUserInSecurityContext(UserDetails userDetails) {
        // Creates an authentication token with the user details provided.
        // The third argument represents the authorities (roles or actions) associated with the user.
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,
                                                        null,
                                                        userDetails.getAuthorities());

        // Sets the authentication token in the Spring Security context.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
