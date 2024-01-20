package co.cuencas.usersadmin.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO
 * Guardarlo en el contexto de spring security
 * Permite utilizar la información que nosotros obtengamos de la base de datos, guardarla en este tipo de dato que es
 * userDetails, y posteriormente realizar la autenticación con Spring Security para obtener toda la información del
 * usuario ya que lo vamos a tener almacenado en Spring Security
 */

public class UserDetailsImpl implements UserDetails {
    /**
     * Atributo para el nombre de usuario
     */
    private final String identification;

    /**
     * Atributo para la contraseña
     */
    private String password;

    /**
     * Atributo para los roles
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor to create an object with the user's identification and its authorities.
     *
     * @param identification User's identification
     * @param authorities    User's authorities
     */
    public UserDetailsImpl(String identification, Collection<? extends GrantedAuthority> authorities) {
        this.identification = identification;
        this.authorities = authorities;
    }

    /**
     * Method to build the user using their information
     *
     * @param user user to build
     * @return new user
     */
    public static UserDetailsImpl buildUserDetails(UserApp user) {
        // Generate a list with the user's authorities depending on their role
        // EN LA LISTA DE AUTHORITIES SE PONE EL ROL DEL USUARIO ¿SE PUEDE PONER LAS ACCIONES?
        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().getRole().name()));

        return new UserDetailsImpl(user.getIdentification(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return identification;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
