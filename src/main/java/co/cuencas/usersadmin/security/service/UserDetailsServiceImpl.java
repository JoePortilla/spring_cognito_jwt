package co.cuencas.usersadmin.security.service;

import co.cuencas.usersadmin.security.entity.UserApp;
import co.cuencas.usersadmin.security.entity.UserDetailsImpl;
import co.cuencas.usersadmin.security.service.UseCase.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    /**
     * Metodo para cargar el usuario por su username
     */
    @Override
    public UserDetails loadUserByUsername(String identification) throws UsernameNotFoundException {
        // Search for the user in the database
        UserApp user = userService.getUserByIdentification(identification)
                                  .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con la c.c.:" +
                                                                                           " " + identification));
        // Build the user details with their username (i.e.: identification) and their authorizations.
        return UserDetailsImpl.build(user);
    }
}
